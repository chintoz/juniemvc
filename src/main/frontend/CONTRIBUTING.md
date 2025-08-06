# Contributing to JunieMVC Frontend

This document provides guidelines for contributing to the JunieMVC frontend codebase.

## Code Style and Standards

### TypeScript

- Use TypeScript for all new code
- Ensure strict type checking is enabled
- Avoid using `any` type when possible
- Use interfaces for object shapes and types for unions/primitives
- Use type inference where it makes code more readable

### React

- Use functional components with hooks
- Avoid class components
- Keep components small and focused on a single responsibility
- Use React.memo for performance optimization when appropriate
- Use proper prop typing with TypeScript

### File Organization

- Group related files in directories
- Use consistent naming conventions:
  - Component files: PascalCase (e.g., `BeerList.tsx`)
  - Hook files: camelCase with 'use' prefix (e.g., `useBeer.ts`)
  - Service files: camelCase (e.g., `beerService.ts`)
  - Test files: Same name as the file they test with `.test` or `.spec` suffix

### CSS/Styling

- Use Tailwind CSS for styling
- Follow utility-first approach
- Use Shadcn UI components when possible
- Create custom components for reused UI patterns

## Development Workflow

### Feature Development

1. Create a new branch from `main` with a descriptive name
2. Implement the feature with appropriate tests
3. Ensure all tests pass
4. Submit a pull request

### Code Quality

- Run linting before committing: `npm run lint`
- Format code: `npm run format`
- Ensure all tests pass: `npm test`
- Fix TypeScript errors: `npm run build`

### Testing

- Write tests for all new features
- Aim for high test coverage
- Test both success and error cases
- Mock external dependencies

#### Types of Tests

- **Unit Tests**: Test individual components, hooks, and services in isolation
- **Integration Tests**: Test interactions between components
- **End-to-End Tests**: Test complete user flows

## Pull Request Process

1. Ensure all tests pass
2. Update documentation if necessary
3. Describe the changes in the pull request description
4. Request review from at least one team member
5. Address review comments
6. Merge once approved

## Best Practices

### State Management

- Use React hooks for local state
- Consider context API for shared state
- Avoid prop drilling
- Keep state as close as possible to where it's used

### API Calls

- Use the service layer for API calls
- Handle loading and error states
- Use custom hooks to encapsulate API logic
- Implement proper error handling

### Performance

- Use React.memo for expensive components
- Avoid unnecessary re-renders
- Use useCallback and useMemo appropriately
- Implement pagination for large data sets
- Use virtualization for long lists

### Accessibility

- Use semantic HTML
- Ensure keyboard navigation works
- Add appropriate ARIA attributes
- Test with screen readers
- Maintain sufficient color contrast

### Security

- Sanitize user input
- Don't store sensitive information in local storage
- Use HTTPS for all API calls
- Implement proper authentication and authorization

## Documentation

- Document complex logic with comments
- Use JSDoc for functions and components
- Keep README.md up to date
- Document API integrations

## Commit Guidelines

- Write clear, concise commit messages
- Use present tense ("Add feature" not "Added feature")
- Reference issue numbers when applicable
- Keep commits focused on a single change

## Versioning

We use [Semantic Versioning](https://semver.org/) for versioning:

- MAJOR version for incompatible API changes
- MINOR version for backwards-compatible functionality
- PATCH version for backwards-compatible bug fixes