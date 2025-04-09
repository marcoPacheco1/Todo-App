
import todoApi from "../../api/TodoApi"
import { TodoProvider } from "../../context/TodoProvider"
import { AddTodoButton } from "./AddTodoButton"
import { Metrics } from "./Metrics"
import { Navbar } from "./Navbar"
import { SearchForm } from "./SearchForm"
import { TodoTable } from "./TodoTable"
import { parseISO } from "date-fns"
import { useContext, useEffect, useMemo, useState } from "react";
import { TodoContext } from "../../context/TodoContext";


export const TodoPage = () => {
  const {allTodos, todos, dispatch, setAllTodos, isLoading } = useContext( TodoContext );
  console.log('PAGE:',todos);
  

  return (
    <>
      <Navbar />
      <SearchForm />
      <AddTodoButton />
      {isLoading && <h2>Loading...</h2>} 
      <TodoTable  />
      <Metrics />
    </>
  )
}
