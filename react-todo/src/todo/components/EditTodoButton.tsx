import React from 'react'; // It's necessary to declare the unit tests.

import { useContext, useState } from "react";
import { TodoModal } from "./TodoModal";
import { TodoContext } from "../../context/TodoContext";
import { TodoInterface } from '../interfaces/TodoInterface';

export const EditTodoButton = ({id}:{ id: string }) => {

  const [modalIsOpen, setModalIsOpen] = useState(false);
  const {  filteredList } = useContext( TodoContext );
  const [todoToEdit, setTodoToEdit] = useState(null);

  const handleOpen = () => {
    const todo = filteredList.find((todo:TodoInterface) => todo.id === id);
    
    setTodoToEdit(todo);
  
    setModalIsOpen(true)
  };
  const handleClose = () => setModalIsOpen(false);


  return (
    <>
      <button
          className="btn btn-primary"
          onClick={ handleOpen }
      >Editar
      </button>
      {modalIsOpen && (
        <TodoModal
          modalIsOpen={modalIsOpen}
          handleClose={handleClose}
          todo={todoToEdit}
        />
      )}
    </>
    
  )
}
