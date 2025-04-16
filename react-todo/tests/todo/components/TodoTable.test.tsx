import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import { TodoTable } from '../../../src/todo/components/TodoTable';
import { TodoContext } from '../../../src/context/TodoContext';

// Mocks para los métodos del contexto
const mockDeleteTodo = jest.fn();
const mockUpdateTodoDone = jest.fn();
const mockSetFilteredList = jest.fn();
const mockSetPaginationModel = jest.fn();
const mockSetSortModel = jest.fn();
const mockSetSelectedRows = jest.fn();

// Datos simulados
const mockTodos = [
  { id: 1, taskName: 'Task A', priority: 'High', dueDate: '2025-04-20' },
  { id: 2, taskName: 'Task B', priority: 'Medium', dueDate: '2025-05-01' },
];

const contextValue = {
  todos: mockTodos,
  filteredList: mockTodos,
  dispatch: jest.fn(),
  deleteTodo: mockDeleteTodo,
  getById: jest.fn(),
  updateTodo: jest.fn(),
  updateTodoDone: mockUpdateTodoDone,
  getAll: jest.fn(),
  paginationModel: { page: 0, pageSize: 10 },
  setPaginationModel: mockSetPaginationModel,
  rowCount: 2,
  sortModel: [],
  setSortModel: mockSetSortModel,
  selectedRows: [],
  setSelectedRows: mockSetSelectedRows,
  setFilteredList: mockSetFilteredList,
};

const renderComponent = () =>
  render(
    <TodoContext.Provider value={contextValue}>
      <TodoTable />
    </TodoContext.Provider>
  );

describe('TodoTable component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders the DataGrid with rows', () => {
    renderComponent();
    expect(screen.getByText('Task A')).toBeInTheDocument();
    expect(screen.getByText('Task B')).toBeInTheDocument();
  });

  test('renders Delete button and calls deleteTodo on click', () => {
    renderComponent();
    const deleteButtons = screen.getAllByRole('button', { name: /delete/i });
    fireEvent.click(deleteButtons[0]);
    expect(mockDeleteTodo).toHaveBeenCalledWith(1);
    expect(mockSetFilteredList).toHaveBeenCalled();
  });

});
