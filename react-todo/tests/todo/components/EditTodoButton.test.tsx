import { EditTodoButton } from '/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/EditTodoButton'
import { render, fireEvent, screen } from '@testing-library/react';
import { TodoContext } from '../../../src/context/TodoContext';
import '@testing-library/jest-dom';
// Mock del componente TodoModal

jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/TodoModal', () => ({
  TodoModal: ({ modalIsOpen, handleClose, todo }) => (
    <div data-testid="todo-modal">
      <span>{todo?.taskName}</span>
      <button onClick={handleClose}>Close</button>
    </div>
  ),
}));

const mockTodos = [
  { id: '1', taskName: 'Test Todo 1', priority: 'Low', dueDate: new Date(), done: false },
  { id: '2', taskName: 'Test Todo 2', priority: 'High', dueDate: new Date(), done: false },
];

const renderComponent = (id: string) => {
  render(
    <TodoContext.Provider value={{ filteredList: mockTodos }}>
      <EditTodoButton id={id} />
    </TodoContext.Provider>
  );
};
describe('EditTodoButton', () => {

  test('renders the edit button', () => {
    const container = renderComponent();
    expect( container ).toMatchSnapshot();
  });

  test('opens modal with correct todo when button is clicked', () => {
    renderComponent('2'); // usamos string '2' porque ids son strings

    fireEvent.click(screen.getByRole('button', { name: /editar/i }));

    expect(screen.getByTestId('todo-modal')).toBeInTheDocument();
    expect(screen.getByText(/Test Todo 2/i)).toBeInTheDocument();
  });
});