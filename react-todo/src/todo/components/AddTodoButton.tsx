import React from 'react'; // It's necessary to declare the unit tests.
import { useState } from "react";
import { TodoModal } from "./TodoModal";

export const AddTodoButton = () => {
  const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
  const handleOpen = () => {
    setModalIsOpen(true);
    console.log(modalIsOpen);
    
  } 
  const handleClose = () => setModalIsOpen(false);

  return (
    <>
      <button
          className="btn btn-primary m-4"
          onClick={ handleOpen }   
      >Add New TODO
      </button>
      {modalIsOpen && (
        <TodoModal
          modalIsOpen={modalIsOpen}
          handleClose={handleClose}
        />
      )}
    </>
    
  )
}
