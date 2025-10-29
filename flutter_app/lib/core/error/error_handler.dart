import 'package:dartz/dartz.dart';
import 'package:http/http.dart' as http;

import 'exceptions.dart';
import 'failures.dart';

class ErrorHandler {
  static Either<Failure, T> handleException<T>(Exception exception) {
    if (exception is ServerException) {
      return Left(ServerFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is NetworkException) {
      return Left(NetworkFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is CacheException) {
      return Left(CacheFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is ValidationException) {
      return Left(ValidationFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is NotFoundException) {
      return Left(NotFoundFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is UnauthorizedException) {
      return Left(UnauthorizedFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is ForbiddenException) {
      return Left(ForbiddenFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else if (exception is TimeoutException) {
      return Left(TimeoutFailure(
        message: exception.message,
        code: exception.code,
      ));
    } else {
      return Left(UnknownFailure(
        message: exception.message,
        code: exception.code,
      ));
    }
  }

  static Either<Failure, T> handleHttpError<T>(http.Response response) {
    switch (response.statusCode) {
      case 200:
      case 201:
        return Right(response as T);
      case 400:
        return Left(ValidationFailure(
          message: 'Bad Request',
          code: '400',
        ));
      case 401:
        return Left(UnauthorizedFailure(
          message: 'Unauthorized',
          code: '401',
        ));
      case 403:
        return Left(ForbiddenFailure(
          message: 'Forbidden',
          code: '403',
        ));
      case 404:
        return Left(NotFoundFailure(
          message: 'Not Found',
          code: '404',
        ));
      case 408:
        return Left(TimeoutFailure(
          message: 'Request Timeout',
          code: '408',
        ));
      case 429:
        return Left(ServerFailure(
          message: 'Too Many Requests',
          code: '429',
        ));
      case 500:
        return Left(ServerFailure(
          message: 'Internal Server Error',
          code: '500',
        ));
      case 502:
        return Left(ServerFailure(
          message: 'Bad Gateway',
          code: '502',
        ));
      case 503:
        return Left(ServerFailure(
          message: 'Service Unavailable',
          code: '503',
        ));
      case 504:
        return Left(ServerFailure(
          message: 'Gateway Timeout',
          code: '504',
        ));
      default:
        return Left(UnknownFailure(
          message: 'Unknown Error',
          code: response.statusCode.toString(),
        ));
    }
  }

  static Either<Failure, T> handleExceptionWithMessage<T>(
    Exception exception,
    String message,
  ) {
    return Left(UnknownFailure(
      message: message,
      code: exception.code,
    ));
  }

  static String getErrorMessage(Failure failure) {
    switch (failure.runtimeType) {
      case ServerFailure:
        return 'Server error: ${failure.message}';
      case NetworkFailure:
        return 'Network error: ${failure.message}';
      case CacheFailure:
        return 'Cache error: ${failure.message}';
      case ValidationFailure:
        return 'Validation error: ${failure.message}';
      case NotFoundFailure:
        return 'Not found: ${failure.message}';
      case UnauthorizedFailure:
        return 'Unauthorized: ${failure.message}';
      case ForbiddenFailure:
        return 'Forbidden: ${failure.message}';
      case TimeoutFailure:
        return 'Timeout: ${failure.message}';
      case UnknownFailure:
        return 'Unknown error: ${failure.message}';
      default:
        return 'An error occurred: ${failure.message}';
    }
  }
}
