import React from 'react'; // It's necessary to declare the unit tests.
import { render, fireEvent, screen } from '@testing-library/react';
import { TodoContext } from '../../../src/context/TodoContext';
import '@testing-library/jest-dom';
import { TodoPage } from '../../../src/todo/components/TodoPage';
import { MemoryRouter } from 'react-router';

jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/Navbar', () => ({
  Navbar: () => <div>Navbar</div>
}));
jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/AddTodoButton', () => ({
  AddTodoButton: () => <div>AddTodoButton</div>
}));
jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/TodoTable', () => ({
  TodoTable: () => <div>TodoTable</div>
}));
jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/AddTodoButton', () => ({
  AddTodoButton: () => <div>TodoTable</div>
}));

jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/Metrics', () => ({
  Metrics: () => <div>Metrics</div>
}));



describe('TodoPage', () => {
  // const renderComponent = (isLoading = false) => {
  //   return render(
  //     <TodoContext.Provider value={{ isLoading }}>
  //       <TodoPage />
  //     </TodoContext.Provider>
  //   );
  // };

  const renderComponent = (isLoading: boolean) => {
    render(
        <TodoContext.Provider value={{ isLoading }}>
            <MemoryRouter>
                <TodoPage />
            </MemoryRouter>
        </TodoContext.Provider>
    );
  };
 
 

  test('renders loading message when isLoading is true', () => {
    renderComponent(true);
    expect(screen.getByText(/Loading.../i)).toBeInTheDocument();
  });

  test('does not render loading message when isLoading is false', () => {
    renderComponent(false);
    expect(screen.queryByText(/Loading.../i)).not.toBeInTheDocument();
  });
});