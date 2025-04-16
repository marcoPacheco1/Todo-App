import { render, fireEvent, screen } from '@testing-library/react';
import { TodoContext } from '../../../src/context/TodoContext';
import '@testing-library/jest-dom';
import { Metrics } from '../../../src/todo/components/Metrics';

describe('Metrics', () => {
  const mockMetricModel = {
    averageEstimatedTimeToComplete: '2 hours',
    averageTimeToFinishByPriority: {
      High: '10 hour 5 minutes',
      Medium: '2 hours',
      Low: '3 hours'
    }
  };

  const renderComponent = () => {
    return render(
      <TodoContext.Provider value={{ metricModel: mockMetricModel }}>
        <Metrics />
      </TodoContext.Provider>
    );
  };

  test('renders the metrics button', () => {
    const { container } = renderComponent();
    expect( container ).toMatchSnapshot();
  });

  test('renders averageEstimatedTimeToComplete correctly', () => {
    renderComponent();
    expect(screen.getByText(/Average time to finish tasks:/i)).toBeInTheDocument();
    expect(screen.getByText('2 hours')).toBeInTheDocument();
  });

  test('renders averageTimeToFinishByPriority correctly', () => {
    renderComponent();
    expect(screen.getByText(/High:/i)).toHaveTextContent('High: 10 hour 5 minutes');
    expect(screen.getByText(/Medium:/i)).toHaveTextContent('Medium: 2 hours');
    expect(screen.getByText(/Low:/i)).toHaveTextContent('Low: 3 hours');
  });

});