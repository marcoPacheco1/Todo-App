import { TodoProvider } from "../../context/TodoProvider"
import { AddTodoButton } from "./AddTodoButton"
import { Metrics } from "./Metrics"
import { Navbar } from "./Navbar"
import { SearchForm } from "./SearchForm"
import { TodoModal } from "./TodoModal"
import { TodoTable } from "./TodoTable"


export const TodoPage = () => {
  return (
    <TodoProvider>
        <Navbar />
        <SearchForm />
        <AddTodoButton />
        <TodoTable  />
        <Metrics />
    </TodoProvider>
  )
}
