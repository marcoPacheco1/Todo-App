import React from 'react'; // It's necessary to declare the unit tests.

import '@testing-library/jest-dom';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { TodoModal } from '../../../src/todo/components/TodoModal';
import { TodoContext } from '../../../src/context/TodoContext';


const mockHandleClose = jest.fn();
const mockPostTodo = jest.fn();
const mockUpdateTodo = jest.fn();
const mockDispatch = jest.fn();
const mockGetAll = jest.fn();

const renderComponent = (props = {}) => {
return render(
        <TodoContext.Provider
            value={{
                todos: [],
                filteredList: [],
                setFilteredList: jest.fn(),
                dispatch: mockDispatch,
                getAll: mockGetAll,
                postTodo: mockPostTodo,
                updateTodo: mockUpdateTodo,
            }}
        >
            <TodoModal
                modalIsOpen={true}
                handleClose={mockHandleClose}
                todo={undefined}
                {...props}
            />`
        </TodoContext.Provider>
    );
};
  
  describe('TodoModal', () => {
    beforeEach(() => {
      jest.clearAllMocks();
    });
  
    test('renders modal with form fields', () => {
      renderComponent();
  
      expect(screen.getByLabelText(/task name/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/priority/i)).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /save/i })).toBeInTheDocument();
    });
  
    test('fills and submits form with correct data', async () => {
      renderComponent();
  
      const taskNameInput = screen.getByLabelText(/task name/i);
      const prioritySelect = screen.getByLabelText(/priority/i);
      const submitButton = screen.getByRole('button', { name: /save/i });
  
      fireEvent.change(taskNameInput, { target: { value: 'New Task' } });
      fireEvent.mouseDown(prioritySelect);
      fireEvent.click(screen.getByRole('option', { name: 'High' }));
  
      fireEvent.click(submitButton);
  
      await waitFor(() => {
        expect(mockPostTodo).toHaveBeenCalledWith(
          expect.objectContaining({
            taskName: 'New Task',
            priority: 'High',
            dueDate: expect.any(Date),
            done: false,
          })
        );
        expect(mockHandleClose).toHaveBeenCalled();
      });
    });
  
    test('calls handleClose on cancel button', () => {
      renderComponent();
  
      const cancelButton = screen.getByRole('button', { name: /cancel/i });
      fireEvent.click(cancelButton);
  
      expect(mockHandleClose).toHaveBeenCalled();
    });
  
    test('clears date when clear button is clicked', async () => {
      renderComponent();
  
      const clearButton = screen.getByRole('button', { name: /clean/i });
      fireEvent.click(clearButton);
  
      await waitFor(() => {
        expect(mockPostTodo).not.toHaveBeenCalled();
      });
    });

    test('Do not send data when form is submitted without input', async () => {
      renderComponent();
      const submitButton = screen.getByRole('button', { name: /Save/i });
      fireEvent.click(submitButton);
      await waitFor(() => {
        expect(mockPostTodo).not.toHaveBeenCalled();
        expect(mockUpdateTodo).not.toHaveBeenCalled();
      });
    });

    test('Call updateTodo function if si the modal include an id (Edit)', async () => {
      const todo = { id: 123, taskName: 'Task before', priority: 'Medium', dueDate: new Date(), done: false };
      renderComponent({ todo });
      const taskNameInput = screen.getByLabelText(/task name/i);
      fireEvent.change(taskNameInput, { target: { value: 'Task was edited' } });
      const submitButton = screen.getByRole('button', { name: /Save/i });
      fireEvent.click(submitButton);
      await waitFor(() => {
        expect(mockUpdateTodo).toHaveBeenCalledWith(
          expect.objectContaining({
            taskName: 'Task was edited',
            id: 123,
          })
        );
        expect(mockHandleClose).toHaveBeenCalled();
      });
    });

    test('shows is-invalid class if the name is empty after submit', async () => {
      renderComponent();
      const submitButton = screen.getByRole('button', { name: /Save/i });
      fireEvent.click(submitButton);
      const taskNameInput = screen.getByLabelText(/Task name/i);
      expect(taskNameInput.parentElement).toHaveClass('is-invalid'); // Verifica la clase en el contenedor
    });

    test('Loads initial values if a todo object is provided (edit mode)', () => {
      const todo = { id: 1, taskName: 'Tarea inicial', priority: 'Low', dueDate: new Date(), done: false };
      renderComponent({ todo });
      expect(screen.getByDisplayValue('Tarea inicial')).toBeInTheDocument();
      expect(screen.getByDisplayValue('Low')).toBeInTheDocument();
    });
});

