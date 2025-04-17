## Breakable Toy I - Todo App Frontend

This is the frontend for the Todo List Manager built using React, Vite, MUI (Material UI), and TypeScript. It communicates with a Spring Boot backend via REST API and allows users to manage their daily tasks efficiently.

### Features

- Create, edit, and delete todos
- Mark todos as done/undone
- Filter and search by name, priority, and done status
- Sort by priority and due date
- Paginate the list of todos
- Visual indicators based on due dates
- View metrics (average time to complete tasks)
- Modal forms for creating and editing todos

### Stack

- React 19
- Vite
- Material UI (MUI)
- TypeScript
- Axios for API communication
- Date-fns for date handling
- React Router
- Jest and React Testing Library for testing

### Installation

1. Clone the repo:
```
git clone https://github.com/marcoPacheco1/Todo-App.git
cd react-todo
```
2. Set up environment variables:
Copy the .env.template file and rename it to .env, then set your backend endpoint in VITE_API_URL:
```
VITE_API_URL=http://localhost:9090/todos
```
3. Install dependencies:
```
npm install
```
4. Start development server:
```
npm run dev
```
The app will be available at http://localhost:8080

### Running tests
```
npm run test
```
This runs the test suite using Jest and React Testing Library.
