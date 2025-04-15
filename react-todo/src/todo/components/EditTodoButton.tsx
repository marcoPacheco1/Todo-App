import { useContext, useState } from "react";
import { TodoModal } from "./TodoModal";
import { TodoContext } from "../../context/TodoContext";

export const EditTodoButton = ({id}:Number) => {

  const [modalIsOpen, setModalIsOpen] = useState(false);
  const {  todos } = useContext( TodoContext );
  const [todoToEdit, setTodoToEdit] = useState(null);

  const handleOpen = () => {
    const todo = todos.find(todo => todo.id === id);
    
    setTodoToEdit(todo);
  
    setModalIsOpen(true)
  };
  const handleClose = () => setModalIsOpen(false);


  return (
    <>
      <button
          className="btn btn-secondary fab"
          onClick={ handleOpen }
      >
          <i className="fas fa-edit"></i>
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
