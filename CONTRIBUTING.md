# Contributing to Simon Kerstan EE Express

Please read these guidelines before contributing to this software to prevent unnecessary (angry) conversations.

## Filing bugs/issues

Feel free to file bugs/issues if something is working unexpectedly or if you have a great new feature idea.

1. Pick a short, simple and meaningful title for the issue.
2. Describe the expected behavior in some short sentences as least technically as possible.
3. If you think it is a bug, describe the current situation and outline the difference to your expectation.
4. If you are unsure whether the issue makes sense or not, please tag it with `question`.
5. Tag your issue with `bug` or `feature`.
6. Be open for developer's responses and try to answer as soon as possible if there are any questions back to you.

## Contributing code

Also, you are free to contribute code to the software. Please follow some easy rules:

1. File a new issue before you begin and describe what you want to do.
2. Assign the created issue to yourself.
3. Work in a new branch either named `feature/xxx` (features) or `hotfix/xxx` (bugs).
4. Follow the [coding guidelines](#coding-guidelines).
5. Write [unit tests](#unit-tests) (if necessary) and [integration tests](#integration-tests).
6. Update the [changelog](CHANGELOG.md). Please note that the changelog only contains changes for public APIs and no
   internal changes.
7. Create a pull request for your created branch against the main branch.
8. Be friendly and open for feedback from other developers and maintainers.

## Coding guidelines

When writing code, be sure to follow some easy guidelines:

- Write object-oriented code using SOLID and DRY principles.
- Use existing frameworks as much as possible but decouple your code when it is easily possible.
- Do not write your own frameworks for existing functionality or give a strong explanation why you did it nevertheless.
- Try to follow the code patterns in the existing source files.
- Separate business logic from presentation logic (e.g., using Model-View-Controller) and use meaningful abstraction in
  your code.
- Use composition over inheritance as much as possible.
- Remember Clean Code (from Robert C. Martin) and use design patterns.

### Unit tests

Unit tests follow the FIRST principles and are written for business logic in the application which can be tested in
isolation. Not every functionality needs unit tests (for example, basic CRUD logic without any specifics does not need
unit tests). Have a look at the rest of the code for an idea about when to write unit tests.

### Integration tests

Integration tests are used to test the application in a full context for an as realistic test environment as possible.
Every functionality can be tested in an at least simple integration test, so please create one to have refactoring
stability at a higher level.
