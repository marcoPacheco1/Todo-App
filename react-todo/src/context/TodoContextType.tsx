import { Dispatch, SetStateAction } from "react";
import { TodoInterface } from "../todo/interfaces/TodoInterface";
import { GridSortModel } from "@mui/x-data-grid";
import { MetricsInterface } from "../todo/interfaces/MetricsInterface";

export interface TodoContextType {
    filteredList: TodoInterface[];
    setFilteredList: Dispatch<SetStateAction<TodoInterface[]>>;
    isLoading: boolean;
    getAll: (params?: URLSearchParams) => Promise<void>;
    postTodo: (formValues: TodoInterface) => Promise<void>;
    updateTodo: (formValues: TodoInterface) => Promise<void>;
    deleteTodo: (id: string) => Promise<void>;
    getById: (id: string) => Promise<TodoInterface | undefined>;
    paginationModel: { pageSize: number; page: number };
    rowCount: number;
    setPaginationModel: Dispatch<SetStateAction<{ pageSize: number; page: number }>>;
    sortModel: GridSortModel;
    setSortModel: Dispatch<SetStateAction<GridSortModel>>;
    metricModel: MetricsInterface;
    updateTodoDone: (ids: string[]) => Promise<void>;
    selectedRows: string[];
    setSelectedRows: Dispatch<SetStateAction<string[]>>;
}


export const defaultContext: TodoContextType = {
    filteredList: [],
    setFilteredList: () => {},
    isLoading: false,
    getAll: async () => {},
    postTodo: async () => {},
    updateTodo: async () => {},
    deleteTodo: async () => {},
    getById: async () => undefined,
    paginationModel: { pageSize: 10, page: 0 },
    rowCount: 0,
    setPaginationModel: () => {},
    sortModel: [],
    setSortModel: () => {},
    metricModel: {
      averageTimeToFinishByPriority: { LOW: '', MEDIUM: '', HIGH: '' },
      averageEstimatedTimeToComplete: '',
    },
    updateTodoDone: async () => {},
    selectedRows: [],
    setSelectedRows: () => {},
};