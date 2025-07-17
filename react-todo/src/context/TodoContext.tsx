import { createContext } from "react";
import { defaultContext, TodoContextType } from "./TodoContextType";


export const TodoContext = createContext<TodoContextType>(defaultContext);


// export const defaultContext: TodoContextType = {
//     filteredList: [],
//     setFilteredList: () => {},
//     isLoading: false,
//     getAll: async () => {},
//     postTodo: async () => {},
//     updateTodo: async () => {},
//     deleteTodo: async () => {},
//     getById: async () => undefined,
//     paginationModel: { pageSize: 10, page: 0 },
//     rowCount: 0,
//     setPaginationModel: () => {},
//     sortModel: [],
//     setSortModel: () => {},
//     metricModel: {
//       averageTimeToFinishByPriority: { Low: '', Medium: '', High: '' },
//       averageEstimatedTimeToComplete: '',
//     },
//     updateTodoDone: async () => {},
//     selectedRows: [],
//     setSelectedRows: () => {},
// };