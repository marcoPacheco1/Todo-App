import { TodoProvider } from "../../context/TodoProvider"
import { AddNewTodo } from "./AddNewTodo"
import { Metrics } from "./Metrics"
import { Navbar } from "./Navbar"
import { SearchForm } from "./SearchForm"
import { TodoTable } from "./TodoTable"


export const TodoPage = () => {
  return (
    <TodoProvider>
        <Navbar />
        <SearchForm />
        <AddNewTodo />
        <TodoTable  />
        <Metrics />
    </TodoProvider>
  )
}
