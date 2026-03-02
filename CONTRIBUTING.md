# Contributing Guide

## Getting Started

1. Fork the repository
2. Clone your fork locally
3. Create a feature branch
4. Make your changes
5. Push to your fork
6. Submit a Pull Request

## Development Setup

### Backend

```bash
cd backend/api
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

### Database

```bash
docker-compose up -d
```

## Code Style Guidelines

### Java

- Use clean code principles
- Follow Spring Boot conventions
- Write tests for all business logic
- Use meaningful variable and method names
- Keep methods small and focused

### JavaScript/Vue

- Use Vue 3 Composition API or Options API
- Follow ESLint rules
- Write descriptive component names
- Comment complex logic
- Use destructuring for imports

## Commit Messages

Use conventional commits:

- `feat:` for new features
- `fix:` for bug fixes
- `docs:` for documentation
- `test:` for tests
- `refactor:` for code refactoring
- `chore:` for maintenance tasks

Example:

```
feat: add production optimization algorithm

- Implement greedy algorithm for production optimization
- Add tests for optimization service
- Update API documentation
```

## Testing

### Backend

```bash
mvn test
```

### Frontend

```bash
npm run test
npm run test:ui
```

## Submitting PR

1. Ensure all tests pass
2. Update documentation if needed
3. Provide clear description of changes
4. Link related issues

## Reporting Issues

Please include:

- Clear title
- Detailed description
- Steps to reproduce
- Expected vs actual behavior
- Screenshots if applicable
- Environment details (OS, Java version, Node version, etc.)
