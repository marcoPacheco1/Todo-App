import { useContext, useState } from "react";
import { TodoContext } from "../../context/TodoContext";
import { differenceInMinutes, differenceInSeconds, format } from "date-fns";
import { TodoInterface } from "../interfaces/TodoInterface";

// TODO: que sea Dinamico
export const Metrics = () => {

  const { todos } = useContext( TodoContext );

  const [averageTimeByPriority, setAverageTimeByPriority] = useState<{
    Low: string;
    Medium: string;
    High: string;
  }>({ Low: '00:00', Medium: '00:00', High: '00:00' });

  const [averagTime, setAveragTime] = useState(0);

  const result = differenceInMinutes(
    new Date(2014, 6, 2, 12, 20, 0),
    new Date(2014, 6, 2, 12, 7, 59)
  )

  const formatSecondsToHHMMSS = (totalSeconds: number): string => {
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;
  
    const formattedHours = String(hours).padStart(2, '0');
    const formattedMinutes = String(minutes).padStart(2, '0');
    const formattedSeconds = String(seconds).padStart(2, '0');
  
    return `${formattedHours}:${formattedMinutes}:${formattedSeconds} HH:MM:SS`;
  };

  const filterTodosByPriority = (todos: TodoInterface[], priority: 'Low' | 'Medium' | 'High'): TodoInterface[] => {
    return todos.filter(todo => todo.priority === priority);
  };

  const calculateAverageTime = (todosList) =>{
    const secondsToFinish: number[] = [];
    todosList.map( (todo:TodoInterface) => {
        const seconds = differenceInSeconds(todo.dueDate, new Date());
        if (seconds> 0)
          secondsToFinish.push(seconds);
        // let a= format((seconds * 1000), 'mm:ss')
        // console.log(a);
        // Use Math.abs to handle past due dates
    });
    let secondsTotalTask = 0;
    for(let i = 0; i < secondsToFinish.length; i++) {
      secondsTotalTask += secondsToFinish[i];
    }
    var avg = secondsTotalTask / secondsToFinish.length;
    const formattedTime = formatSecondsToHHMMSS(avg); 
    console.log(formattedTime);
    return formattedTime;
  }

  const lowPriorityTodos = filterTodosByPriority(todos, 'Low');
  
  console.log('Low Priority Todos:', calculateAverageTime(lowPriorityTodos));

  const highPriorityTodos = filterTodosByPriority(todos, 'High');
  console.log('High Priority Todos:', calculateAverageTime(highPriorityTodos));

  const mediumPriorityTodos = filterTodosByPriority(todos, 'Medium');
  console.log('Medium Priority Todos:', calculateAverageTime(mediumPriorityTodos));

  return (
    <>
      <div>Average</div>
      <p>{calculateAverageTime(todos)}</p>
      <div>High</div>
      <p>{calculateAverageTime(highPriorityTodos)}</p>
      <div>Medium</div>
      <p>{calculateAverageTime(mediumPriorityTodos)}</p>
      <div>Low</div>
      <p>{calculateAverageTime(lowPriorityTodos)}</p>
    </>
  )
}
