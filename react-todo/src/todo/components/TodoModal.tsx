import { Box, Button, Modal, TextField, Typography } from "@mui/material"
import { addHours, differenceInSeconds } from "date-fns";

import { useContext, useEffect, useMemo, useState } from "react";
import DatePicker from "react-datepicker";

import 'react-datepicker/dist/react-datepicker.css';
import { TodoContext } from "../../context/TodoContext";


const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };

export const TodoModal = ({modalIsOpen, handleClose, todo}) => {
    // const [open, setOpen] = useState(false);
    // const handleOpen = () => setOpen(true);
    // const handleClose = () => setOpen(false);
    const {allTodos, todos, dispatch, setAllTodos } = useContext( TodoContext );
    
    const [ formSubmitted, setFormSubmitted ] = useState(false);

    const [ formValues, setFormValues ] = useState( {
        taskName: '',
        priority:'',
        dueDate: addHours( new Date(), 2),
    } );

    useEffect(() => {
        if (todo !== undefined)
            setFormValues({ ...todo });
    }, [ todo ])
      

    console.log("eveto:",);
    
    // const { taskName, priority, dueDate } = formState;

    
    // Valida que el titulo sea vacio o no
    const titleClass = useMemo(() => {
        if ( !formSubmitted ) return '';

        return ( formValues.taskName.length > 0 )
            ? ''
            : 'is-invalid';

    }, [ formValues.taskName, formSubmitted ])
    
    const onInputChange = ({ target }) => {
        // console.log(target);
        
        const { name, value } = target;
        // console.log(name, value);
        setFormValues({
            ...formValues,
            [ name ]: value
        });
    }


    const onSubmit = async( event ) => {
        console.log("enviado");
        
        event.preventDefault();
        setFormSubmitted(true);

        const difference = differenceInSeconds( formValues.dueDate, new Date() );
        
        if ( isNaN( difference ) || difference <= 0 ) {
            // Swal.fire('Fechas incorrectas','Revisar las fechas ingresadas','error');
            console.log('error fecha');
            
            return;
        }
        
        if ( formValues.taskName.length <= 0 ) return;
        
        // Valores del formulario
        console.log(formValues);

        // TODO: 
        // await startSavingEvent( formValues );
        // llegar al backend
        // Todo bien
        if( formValues.id ) {
            // Actualizando
            const action = {
                type: 'Update Todo',
                payload: formValues
            }
            dispatch( action );


            const updatedAllTodos = allTodos.map(todo =>
                todo.id === formValues.id ? formValues : todo
            );
            setAllTodos(updatedAllTodos); 

        } else {
            // Creando
            const action = {
                type: 'Add Todo',
                payload: formValues
            }
            dispatch( action );
            // dispatch( onAddNewEvent({ ...calendarEvent, _id: new Date().getTime() }) );
        }
        
        setFormSubmitted(false);
        // Aquí puedes enviar los datos del formulario o realizar otras acciones
        handleClose();
        console.log("todos local ");
        console.log(todos);

        console.log("todos global ");
        console.log(allTodos);
    }


    

    const onDateChanged = ( event, changing ) => {
        setFormValues({
            ...formValues,
            // changing es start or end
            [changing]: event
        })
    }
    
  return (
    <>
        {/* <Button onClick={handleOpen}>Open modal</Button> */}
        <Modal
            open={modalIsOpen}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
            >
            <Box sx={style}>
                <Typography id="modal-modal-title" variant="h6" component="h2">
                Task
                </Typography>
                <form onSubmit={onSubmit}>
                    <TextField
                        fullWidth
                        className={ `form-control ${ titleClass }`}
                        margin="normal"
                        id="taskName"
                        name="taskName"
                        label="task name"
                        value={formValues.taskName}
                        onChange={onInputChange}
                    />
                    <TextField
                        fullWidth
                        margin="normal"
                        id="priority"
                        name="priority"
                        label="Prioridad"
                        value={formValues.priority}
                        onChange={onInputChange}
                    />
                    <DatePicker 
                        selected={formValues.dueDate}
                        onChange={ (event) => onDateChanged(event, 'dueDate') }
                        className="form-control"
                        dateFormat="Pp"
                        showTimeSelect
                        timeCaption="Hora"
                    />
                    {/* Agrega más campos de formulario aquí */}
                    <Button type="submit" variant="contained" color="primary">
                    Guardar
                    </Button>
                    <Button onClick={handleClose} color="secondary">
                    Cancelar
                    </Button>
                </form>
            </Box>
        </Modal>
    </>
  )
}
