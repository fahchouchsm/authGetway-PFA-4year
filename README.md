# AuthGateway - Project Overview

## Purpose
AuthGateway is a Spring Boot-based authentication gateway that acts as the entry point (the "door") for communication between clients and microservices. It enforces authentication and authorization using RS256-signed JSON Web Tokens (JWTs). The gateway validates tokens, extracts claims, and forwards authenticated requests to internal microservices.

## Key responsibilities
- Validate incoming JWTs signed with RS256 (RSA + SHA256).
- Expose a secure boundary for microservices (centralized auth).
- Provide token introspection / user claims injection into downstream requests.
- Support JWKS (JSON Web Key Set) or direct RSA public key configuration.
- Offer configurable route protection and role-based access control.

## Architecture (high-level)
- Client -> AuthGateway (this project) -> Internal Microservices
- AuthGateway performs:
  - TLS termination (recommended)
  - JWT validation (RS256)
  - Claim mapping to headers or context
  - Authorization checks (roles/scopes)
  - Routing / forwarding to internal services

## Technologies
- Java \ (Spring Boot)
- Maven
- Java JWT libraries (e.g., `nimbus-jose-jwt` or `spring-security-oauth2-jose`)
- JavaScript / npm (for any frontend or tooling in the repo)
- IntelliJ IDEA (development)
- Windows (developer OS)

## Security and RS256 (how validation works)
1. Issuer signs JWTs using an RSA private key (RS256).
2. AuthGateway validates tokens by verifying the signature using the corresponding RSA public key.
3. Gateway verifies standard claims:
   - `exp` (expiration)
   - `nbf` (not before) optional
   - `iss` (issuer) if configured
   - `aud` (audience) if configured
4. Optionally, Gateway fetches the issuer's JWKS endpoint and caches keys. Keys are selected by the token's `kid` header.
5. After validation, Gateway attaches user claims to the forwarded request (HTTP headers or security context).

## Configuration (example)
- Use JVM environment variables, Spring `application.properties` or `application.yml`.
- Example properties (replace lines in your `application.properties` or `application.yml`):

```properties
# application.properties
server.port=8080
authgateway.jwks-uri=https://issuer.example.com/.well-known/jwks.json
authgateway.expected-issuer=https://issuer.example.com
authgateway.expected-audience=api://default
authgateway.public-key-file=classpath:keys/public.pem