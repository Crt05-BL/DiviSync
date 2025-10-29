import 'package:equatable/equatable.dart';

abstract class Exception extends Equatable {
  final String message;
  final String? code;

  const Exception({
    required this.message,
    this.code,
  });

  @override
  List<Object?> get props => [message, code];
}

class ServerException extends Exception {
  const ServerException({
    required super.message,
    super.code,
  });
}

class NetworkException extends Exception {
  const NetworkException({
    required super.message,
    super.code,
  });
}

class CacheException extends Exception {
  const CacheException({
    required super.message,
    super.code,
  });
}

class ValidationException extends Exception {
  const ValidationException({
    required super.message,
    super.code,
  });
}

class NotFoundException extends Exception {
  const NotFoundException({
    required super.message,
    super.code,
  });
}

class UnauthorizedException extends Exception {
  const UnauthorizedException({
    required super.message,
    super.code,
  });
}

class ForbiddenException extends Exception {
  const ForbiddenException({
    required super.message,
    super.code,
  });
}

class TimeoutException extends Exception {
  const TimeoutException({
    required super.message,
    super.code,
  });
}

class UnknownException extends Exception {
  const UnknownException({
    required super.message,
    super.code,
  });
}
