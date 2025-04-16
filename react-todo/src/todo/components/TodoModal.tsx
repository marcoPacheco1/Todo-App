import React, { ChangeEvent } from 'react'; // It's necessary to declare the unit tests.

import { Box, Button, FormControl, InputLabel, MenuItem, Modal, Select, TextField, Typography } from "@mui/material"
import { addHours, differenceInSeconds } from "date-fns";

import { useContext, useEffect, useMemo, useState } from "react";
import DatePicker from "react-datepicker";

import 'react-datepicker/dist/react-datepicker.css';
import { TodoContext } from "../../context/TodoContext";
import todoApi from "../../api/TodoApi";
import { TodoInterface } from "../interfaces/TodoInterface";

interface MiDatePickerProps {
    formValues: { dueDate: Date | null };
    onDateChanged: (date: Date | null, fieldName: string) => void;
}

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
    const {filteredList, setFilteredList,  todos, dispatch, getAll, postTodo, updateTodo } = useContext( TodoContext );
    
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
    // const { taskName, priority, dueDate } = formState;

    
    // Valida que el titulo sea vacio o no
    const titleClass = useMemo(() => {
        if ( !formSubmitted ) return '';

        return ( formValues.taskName.length > 0 )
            ? ''
            : 'is-invalid';

    }, [ formValues.taskName, formSubmitted ])
    
    const onInputChange = ({ target }: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        // console.log(target);
        
        const { name, value } = target;
        // console.log(name, value);
        setFormValues({
            ...formValues,
            [ name ]: value
        });
    }

    const onSubmit = async( event: React.FormEvent<HTMLFormElement> ) => {
        
        event.preventDefault();
    
        setFormSubmitted(true);

        // const difference = differenceInSeconds( formValues.dueDate, new Date() );
        
        // if ( isNaN( difference ) || difference <= 0 ) {
        //     // Swal.fire('Fechas incorrectas','Revisar las fechas ingresadas','error');
        //     console.log('error fecha');
            
        //     return;
        // }
        
        if ( formValues.taskName.length <= 0 ) return;
        
        
        const newTodoElement:TodoInterface = {
            done: false,
            // priority: action.payload.priority,
            ...formValues
        }
        // Valores del formulario
        console.log("formuarios update", newTodoElement);

        // BACKEND 

        if ('id' in formValues) {
            //update
            updateTodo(newTodoElement)
        }
        else{
            //new
            postTodo(newTodoElement)
        }

        // await startSavingEvent( formValues );
        // const { data } = await todoApi.post('', formValues );

    /*
        if( data.id ) {
            // Actualizando
            const action = {
                type: 'Update Todo',
                payload: formValues
            }
            dispatch( action );

            await getAll();
            // update updatedAllTodos
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
        */
        
        setFormSubmitted(false);
        // Aquí puedes enviar los datos del formulario o realizar otras acciones
        handleClose();        
    }

    const onDateChanged = ( event, changing ) => {
        setFormValues({
            ...formValues,
            [changing]: event
        })
    }
    
    const handleClearDate = () => {
        onDateChanged(null, 'dueDate');
    };
    
    const CustomInput: React.FC<ReactDatePickerProps> = ({ value, onClick }) => (
        <div style={{ display: 'flex', alignItems: 'center' }}>
            <input
            type="text"
            value={value || ''}
            className="form-control"
            onClick={onClick}
            readOnly
            style={{ flex: 1, marginRight: '8px' }}
            />
            <Button onClick={handleClearDate} color="secondary" size="small">
            Limpiar
            </Button>
        </div>
    );
   

  return (
    <>
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
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="priority-select-label">Priority</InputLabel>
                        <Select
                            labelId="priority-select-label"
                            id="priority-select"
                            name="priority"
                            value={formValues.priority}
                            label="Prioridad"
                            onChange={onInputChange}
                        >
                            <MenuItem value="Low">Low</MenuItem>
                            <MenuItem value="Medium">Medium</MenuItem>
                            <MenuItem value="High">High</MenuItem>
                        </Select>
                    </FormControl>
                    {/* <TextField
                        fullWidth
                        margin="normal"
                        id="priority"
                        name="priority"
                        label="Prioridad"
                        value={formValues.priority}
                        onChange={onInputChange}
                    /> */}
                    <DatePicker 
                        selected={formValues.dueDate}
                        onChange={ (event) => onDateChanged(event, 'dueDate') }
                        className="form-control"
                        dateFormat="Pp"
                        showTimeSelect
                        timeCaption="time"
                        customInput={<CustomInput />}

                    />
                    
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
