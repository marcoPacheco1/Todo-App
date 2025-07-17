import React, { ChangeEvent } from 'react'; // It's necessary to declare the unit tests.

import { Box, Button, FormControl, InputLabel, MenuItem, Modal, Select, SelectChangeEvent, TextField, Typography } from "@mui/material"
import { addHours } from "date-fns";

import { useContext, useEffect, useMemo, useState } from "react";
import DatePicker from "react-datepicker";

import 'react-datepicker/dist/react-datepicker.css';
import { TodoContext } from "../../context/TodoContext";
import { TodoInterface } from "../interfaces/TodoInterface";

interface ReactDatePickerProps {
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

export const TodoModal = ({modalIsOpen, handleClose, todo}: {modalIsOpen: boolean; handleClose: () => void; todo?: TodoInterface}) => {
    
    interface FormValues {
        taskName: string;
        priority: string;
        dueDate: Date | null;
    }
    
    const { postTodo, updateTodo } = useContext( TodoContext );
    
    const [ formSubmitted, setFormSubmitted ] = useState(false);

    const [ formValues, setFormValues ] = useState<FormValues>( {
        taskName: '',
        priority:'',
        dueDate: addHours( new Date(), 2),
    } );

    useEffect(() => {
        if (todo !== undefined)
            setFormValues({ ...todo, dueDate: todo.dueDate || null  });
    }, [ todo ])
    
    const titleClass = useMemo(() => {
        if ( !formSubmitted ) return '';

        return ( formValues.taskName.length > 0 )
            ? ''
            : 'is-invalid';

    }, [ formValues.taskName, formSubmitted ])
    
    const onInputChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => {
        const { name, value } = event.target;
    
        setFormValues({
            ...formValues,
            [name]: value,
        });
    };

    // const onInputChange = ({ target }: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    //     const { name, value } = target;
        
    //     setFormValues({
    //         ...formValues,
    //         [ name ]: value
    //     });
    // }

    const onSubmit = async( event: React.FormEvent<HTMLFormElement> ) => {
        
        event.preventDefault();
    
        setFormSubmitted(true);
        
        if ( formValues.taskName.length <= 0 ) return;
        
        
        const newTodoElement:TodoInterface = {
            done: false,
            ...formValues
        }

        // BACKEND 
        if ('id' in formValues) {
            //update
            updateTodo(newTodoElement)
        }
        else{
            //new
            postTodo(newTodoElement)
        }
        
        setFormSubmitted(false);
        handleClose();        
    }

    const onDateChanged = (event: Date | null, changing: keyof FormValues) => {
        setFormValues({
            ...formValues,
            [changing]: event,
        });
    }
    
    const handleClearDate = () => {
        onDateChanged(null, 'dueDate');
    };
    
    const CustomInput: React.FC<{ value?: string; onClick?: () => void }> = ({ value, onClick }) => (
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
            Clean
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
                        label="Task name"
                        value={formValues.taskName}
                        onChange={onInputChange}
                        InputProps={{
                            className: titleClass, // Aplica la clase al contenedor del <input>
                        }}
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
                    Due date:
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
                    Save
                    </Button>
                    <Button onClick={handleClose} color="secondary">
                    Cancel
                    </Button>
                </form>
            </Box>
        </Modal>
    </>
  )
}
