import { useState } from "react";
import { TodoModal } from "./TodoModal";

export const AddTodoButton = () => {
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const handleOpen = () => {
    console.log('model abierto');
    setModalIsOpen(true);
    console.log(modalIsOpen);
    
  } 
  const handleClose = () => setModalIsOpen(false);


  return (
    <>
      <button
          className="btn btn-primary fab"
          onClick={ handleOpen }
      >
          <i className="fas fa-plus"></i>
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
