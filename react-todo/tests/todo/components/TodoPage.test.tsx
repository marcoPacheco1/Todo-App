import React from 'react'; // It's necessary to declare the unit tests.
import { render, fireEvent, screen } from '@testing-library/react';
import { TodoContext } from '../../../src/context/TodoContext';
import '@testing-library/jest-dom';
import { TodoPage } from '../../../src/todo/components/TodoPage';


// jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/SearchForm', () => () => <div>SearchForm</div>);
// jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/AddTodoButton',
//  () => () => <div>AddTodoButton</div>);
// jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/TodoTable', () => () => <div>TodoTable</div>);
// jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/Metrics', () => () => <div>Metrics</div>);


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
  const renderComponent = (isLoading = false) => {
    return render(
      <TodoContext.Provider value={{ isLoading }}>
        <TodoPage />
      </TodoContext.Provider>
    );
  };
 
  test('renders all subcomponents', () => {
    //const { container } = renderComponent();
    //console.log(container);
    //expect( container ).toMatchSnapshot();

    //expect(screen.getByText('Navbar')).toBeInTheDocument();
    // expect(screen.getByText('SearchForm')).toBeInTheDocument();
    // expect(screen.getByText('AddTodoButton')).toBeInTheDocument();
    // expect(screen.getByText('TodoTable')).toBeInTheDocument();
    // expect(screen.getByText('Metrics')).toBeInTheDocument();
  });

  // test('renders loading message when isLoading is true', () => {
  //   renderComponent(true);

  //   expect(screen.getByText(/Loading.../i)).toBeInTheDocument();
  // });

  // test('does not render loading message when isLoading is false', () => {
  //   renderComponent(false);

  //   expect(screen.queryByText(/Loading.../i)).not.toBeInTheDocument();
  // });
});