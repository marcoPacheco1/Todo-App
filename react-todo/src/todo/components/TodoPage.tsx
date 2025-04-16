import React from 'react'; // It's necessary to declare the unit tests.

import todoApi from "../../api/TodoApi"
import { TodoProvider } from "../../context/TodoProvider"
import { AddTodoButton } from "./AddTodoButton"
import { Metrics } from "./Metrics"
import { SearchForm } from "./SearchForm"
import { TodoTable } from "./TodoTable"
import { useContext, useEffect, useMemo, useState } from "react";
import { TodoContext } from "../../context/TodoContext";
import { Navbar } from "./Navbar"


export const TodoPage = () => {
  const {isLoading } = useContext( TodoContext );
  
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
