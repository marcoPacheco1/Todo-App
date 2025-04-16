import { EditTodoButton } from '/Users/marco.pacheco/Code/Repos/React/Todo-App/react-todo/src/todo/components/EditTodoButton'
import { render, fireEvent, screen } from '@testing-library/react';
import { TodoContext } from '../../../src/context/TodoContext';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router';
import { SearchForm } from '../../../src/todo/components/SearchForm';


// Mock navigate and location from react-router
const mockNavigate = jest.fn();
const mockLocation = {
  search: '?done=true&priority=High&name=test'
};

jest.mock('react-router', () => ({
  ...jest.requireActual('react-router'),
  useNavigate: () => mockNavigate,
  useLocation: () => mockLocation,
}));

describe('SearchForm', () => {
  const getAll = jest.fn();
  const dispatch = jest.fn();

  const renderComponent = () => {
    render(
      <BrowserRouter>
        <TodoContext.Provider value={{
          todos: [],
          filteredList: [],
          setFilteredList: jest.fn(),
          dispatch,
          getAll
        }}>
          <SearchForm />
        </TodoContext.Provider>
      </BrowserRouter>
    );
  };

  test('renders correctly with initial values from URL', () => {
    renderComponent();

    expect(screen.getByDisplayValue('test')).toBeInTheDocument(); // taskName
    expect(screen.getByDisplayValue('High')).toBeInTheDocument(); // priority
    expect(screen.getByDisplayValue('Done')).toBeInTheDocument(); // state
  });

  test('calls getAll and navigate on form submit', () => {
    renderComponent();

    const searchButton = screen.getByRole('button', { name: /search/i });
    fireEvent.click(searchButton);

    expect(getAll).toHaveBeenCalled();
    expect(mockNavigate).toHaveBeenCalled();
  });
});