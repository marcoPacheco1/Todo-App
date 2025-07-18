import { createContext } from "react";
import { defaultContext, TodoContextType } from "./TodoContextType";


export const TodoContext = createContext<TodoContextType>(defaultContext);
