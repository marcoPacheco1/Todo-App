import { AddTodoButton } from '/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/AddTodoButton'
import { render, fireEvent, screen } from '@testing-library/react';

// Mock del componente TodoModal

jest.mock('/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/TodoModal', () => ({
    TodoModal: jest.fn(({ modalIsOpen, handleClose }) => (
        <div data-testid="todo-modal">
            {modalIsOpen && <p>Modal is open</p>}
            <button onClick={handleClose}>Close Modal</button>
        </div>
    )),
}));


describe('tests for AddTodoButton component', () => { 
    test('should match with snapshot', () => { 
        const { container } = render(<AddTodoButton />);
        expect( container ).toMatchSnapshot();
    });

    test('should modal be opened', () => { 
        render(<AddTodoButton />);
        fireEvent.click( screen.getByRole('button'));
        expect(screen.getByTestId('todo-modal')).toBeTruthy();
    });

    test('should modal be closed', () => { 
        render(<AddTodoButton />);
        fireEvent.click( screen.getByRole('button'));
        fireEvent.click(screen.getByRole('button', { name: /Close Modal/i }));
        expect(screen.queryByTestId('todo-modal')).toBeNull();
    });
});