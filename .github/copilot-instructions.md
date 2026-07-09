# Pull Request Review Guidelines

In addition to the default review behavior, also analyze:

## Architecture and Design
- SOLID principle violations
- Excessive coupling
- Low cohesion
- God classes/services
- Anemic domain models when applicable

## Clean Code
- Meaningful naming
- Small and focused methods
- Readability and maintainability
- Unnecessary complexity
- Duplicate logic
- Hidden side effects
- Magic numbers and strings

## Backend Best Practices
- Transaction boundary issues
- N+1 query risks
- Improper exception handling
- Misuse of dependency injection
- Mutable shared state
- Poor separation of concerns

Only suggest changes that provide real maintainability, readability, scalability, or reliability improvements.
