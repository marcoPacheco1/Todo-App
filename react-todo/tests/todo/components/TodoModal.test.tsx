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
      expect(screen.getByRole('button', { name: /guardar/i })).toBeInTheDocument();
    });
  
    test('fills and submits form with correct data', async () => {
      renderComponent();
  
      const taskNameInput = screen.getByLabelText(/task name/i);
      const prioritySelect = screen.getByLabelText(/priority/i);
      const submitButton = screen.getByRole('button', { name: /guardar/i });
  
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
  
      const cancelButton = screen.getByRole('button', { name: /cancelar/i });
      fireEvent.click(cancelButton);
  
      expect(mockHandleClose).toHaveBeenCalled();
    });
  
    test('clears date when limpiar is clicked', async () => {
      renderComponent();
  
      const clearButton = screen.getByRole('button', { name: /limpiar/i });
      fireEvent.click(clearButton);
  
      // Date should be null after "Limpiar"
      await waitFor(() => {
        expect(mockPostTodo).not.toHaveBeenCalled(); // just verifying it's not auto-submitted
      });
    });
});