# Text to Image as a Service

This is an example of how to use the data oriented programming in Java
to implement a simple REST API.

This service fakes the creation of images using DALL-E or Stable Diffusion.

The code provides 4 times the same code (the REST controller) starting
with a code using records and refactoring the code using the data oriented
programming.

To run this example
```
  export PATH=/path/to/java19
  mvn spring-boot:run
```

then open http://localhost:8080

## Presentation of the REST API

The REST API is composed of 3 endpoints
- POST api/dalle create an image from a `user`, a `text` and an `image size` using DALL-E
- POST api/sd create an image from a `user`, a `text`, an `image size` and optionally the sampling `plms` using Stable Diffusion
- GET api/invoice return an array of all invoices

The documentation of the REST API using the
[Swagger UI on localhost:8080](http://localhost:8080/swagger-ui/index.html)

## Presentation of the different Java files

- `Engines`, a fake implementation of DALL-E and Stable Difussion text to image algorithms

- `db/Invoice` and `db/InvoiceRepository`, JPA + Spring Data repository for invoices
- `TextToIMageAPI$x` and `Data$x` (with `$x` = 1, 2, 3 or 4), 4 versions of the REST controller and its data definitions

Note: The fact that all data definitions are in one file
is nice for a tutorial, everything is one place, but do not do that in production.


## TextToImageAPI1 + Data1

Goal
- Use records to model the inputs and outputs of the REST endpoints

The input of the DALL-E endpoint is defined as a record
```java
record DalleImageRequest(String user, String text, ImageSize imageSize) {}
```

The input of the Stable Diffusion endpoint is defined as a record
```java
record SDImageRequest(String user, String text, ImageSize imageSize, boolean plms) {}
```

The output of both DALL-E and Stable Diffusion endpoints is defined as a record
```java
record ImageResponse(String imagePath) {}
```

The output of the invoice endpoint is defined as a record
```java
record InvoiceResponse(String userName, String message, int price, LocalDateTime dateTime) {}
```

Conclusion: __Use records to model the inputs and outputs of the REST endpoints__


## TextToImageAPI2 + Data2

Goal
- extract the algorithm that compute the price from the controller

The price depends on the parameters of the algorithm used, so we model
the parameters as records

```java
record DalleEngineParameter(ImageSize imageSize) {}
record SDEngineParameter(ImageSize imageSize, boolean plms) {}
```

We know want to compute the price, for that those parameters need a
common interface.

```java
interface EngineParameter {}
record DalleEngineParameter(ImageSize imageSize) implements EngineParameter {}
record SDEngineParameter(ImageSize imageSize, boolean plms) implements EngineParameter {}
```

The price is a static method that uses a __switch expression__ and __type patterns__,
one for the DALL-E parameters and one for the Stable Diffusion parameter.

```java
static int price(EngineParameter engineParameter) {
  return switch (engineParameter) {
    case DalleEngineParameter __ -> 1_000;
    case SDEngineParameter parameter -> imagePrice(parameter.imageSize()) + plmsPrice(parameter.plms());
    default -> throw new IllegalStateException("Unexpected value: " + engineParameter);
  };
}
```

Note: we use '__' here to indicate that the value is unused, in the future (Java 20),
`_` will be used.

Conclusion: __use pattern matching to define functions over the records__


## TextToImageAPI3 + Data3

Goal
- apply the data oriented programmation technics (exhaustive switch, record patterns)
- refactor the price computation as one giant switch to be more readable

The idea of the Data Oriented Programming is that the data ar more important than
the code so if the data change the code that must be changed should be flagged by
the compiler.

For that, we need to switch of the pattern matching to be exhaustive,
so the compiler will report an error is a new subtypes of `EngineParameter`
is declared and instead of using `Type Patterns`, we should use `Record Patterns`
which assert that the shape of a record. The compiler will report an error if
the shape is change.

### Pattern matching should be exhaustive

First, we need to declare that an `EngineParameter` can only be either a
`DalleEngineParameter` or a `SDEngineParameter` and not other subtypes.

In Java, this is done with the keyword `sealed` and the clause `permits`
that lists all subtypes
```java
sealed interface EngineParameter permits DalleEngineParameter, SDEngineParameter {}
```

Note: if the records and the interface are in the same file, the clause `permits` is
optional because the compiler can compute it.

If `EngineParameter` is sealed, the compiler does not need a `default`, better it
does not compile if there is a `default` because the `default` is unreachable
```java
static int price(EngineParameter engineParameter) {
  return switch (engineParameter) {
    case DalleEngineParameter __ -> 1_000;
    case SDEngineParameter parameter -> imagePrice(parameter.imageSize()) + plmsPrice(parameter.plms());
  };
}
```

### Record Patterns instead of Type Patterns

We also want to be sure that if, for example, the record `DalleEngineParameter`
is changed by adding a component, the switch will not compile. For that we use,
the `Record Pattern` which not only checks the type but also the components and
their types.

```java
static int price(EngineParameter engineParameter) {
  return switch (engineParameter) {
    case DalleEngineParameter(ImageSize __) -> 1_000;
    case SDEngineParameter(ImageSize size, boolean plms) -> imagePrice(size) + plmsPrice(plms);
  };
}
```

We can make the code a little more explicit by specifying all the possible price formula
into one giant switch. For that we use an optional guard `when` after the pattern of a case.

```java
static int price(EngineParameter engineParameter) {
  return switch (engineParameter) {
    case DalleEngineParameter(ImageSize __) -> 1_000;
    case SDEngineParameter(ImageSize size, boolean plms) when size == small && !plms -> 125;
    case SDEngineParameter(ImageSize size, boolean plms) when size == small -> 150;
    case SDEngineParameter(ImageSize size, boolean plms) when size == medium && !plms -> 400;
    case SDEngineParameter(ImageSize size, boolean plms) when size == medium -> 425;
    case SDEngineParameter(ImageSize size, boolean plms) when size == big && !plms -> 800;
    case SDEngineParameter(ImageSize _1, boolean _2) -> 825;
  };
}
```

You can notice that the guard has access to the `bindings` created by the `Record Pattern`.
For example, in the second line, `when` has access to the local variables `size` and `plms`.

Conclusion: __DOP = no default in switch + use record patterns to assert the shape__

## TextToImageAPI4 + Data4

Goal
- Share the code between the DALL-E endpoint and the Stable Diffusion endpoint 

We can now, share the code of _dalle_ and _sd_, by creating an `EnginParameter`
from an `ImageRequest`

```java
static EngineParameter parameter(ImageRequest imageRequest) {
  return switch (imageRequest) {
    case DalleImageRequest request -> new DalleEngineParameter(request.imageSize());
    case SDImageRequest request -> new SDEngineParameter(request.imageSize(), request.plms());
  };
}
```

Obviously, it means that `ImageRequest` is a sealed interface.

We also need a methode `imageToText` that calls the right engines.

```java
static String imageToText(String text, EngineParameter engineParameter) {
  return switch (engineParameter) {
    case DalleEngineParameter(ImageSize imageSize) -> Engines.dall_e(text, imageSize.size(), imageSize.size());
    case SDEngineParameter(ImageSize imageSize, boolean plms) -> Engines.stable_diffusion(text, imageSize.size(), imageSize.size(), plms);
  };
}
```

And to extract the `user` and `text` from both the `ImageRequest`, a simple solution
is to declare the (virtual) accessor in the interface.

```java
private ImageResponse createImage(ImageRequest imageRequest) {
  var user = imageRequest.user();
  var text = imageRequest.text();
  ...
```

In the future, we want to be able to de-structure an interface like `ImageRequest`
to be able to write a code like this.

```java
private ImageResponse createImage(ImageRequest imageRequest) {
  ImageRequest(var user, var text) = imageRequest;
  ...
```

Conclusion: __records are the data and pattern matching in methods computations__
