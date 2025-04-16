import React, { ChangeEvent } from 'react'; // It's necessary to declare the unit tests.

import { useContext, useEffect, useState } from "react";
import { TodoContext } from "../../context/TodoContext";
import { useLocation, useNavigate } from "react-router";
import { FilterForm } from '../interfaces/ModalForm';

export const SearchForm = () => {

    const navigate = useNavigate();
    const location = useLocation();
    interface Params {
        name?: string;
        done?: boolean;
        priority?: string;
        [key: string]: any;
    }
      

    const {filteredList, setFilteredList, todos, dispatch, getAll } = useContext( TodoContext );
    
    const [ formState, setFormState ] = useState<FilterForm>( {
        taskName: '',
        priority: 'All',
        state: 'All'
    } );

    const { taskName, priority, state } = formState;

    const onResetFilter = () => {
        dispatch({ type: 'Reset Todo', payload: allTodos });
    };


    const buildURL = async() => {
        // http://localhost:8080/todos?done=false&page=1&name=pan&priority=Low

        const params:Params = {};

        if (formState.hasOwnProperty('taskName')){
            params.name = formState['taskName'];
        }

        if (formState.hasOwnProperty('state')){
            if (formState['state'] === 'Undone')
                params.done = false 
            else if (formState['state'] === 'Done')
                params.done = true 
        }
        
        if (formState.hasOwnProperty('priority')){
            if (formState['priority'] !== 'All')
                params.priority = formState['priority'];
        }

        const queryParams = new URLSearchParams();

        for (const key in params) {
            if (params.hasOwnProperty(key) && params[key] !== '')
                queryParams.append(key, params[key]);
        }

        const queryString = queryParams.toString();
        const url =`?${queryString.toString()}`;

        navigate(url, { replace: true });
        console.log('parametros URL:', params);
        await getAll(params);

    }

    const onSearchSubmit = async(event: React.FormEvent<HTMLFormElement>) =>{
        event.preventDefault();
        buildURL();
    }

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        console.log(location.search);
        console.log(params.get('done') === 'true');
        
        const newFormState:FilterForm = {
            taskName: params.get('name') || '',
            priority: params.get('priority') || 'All',   
        };
        if (params.get('done') === 'false')
            newFormState.state = 'Undone' ;
        else if (params.get('done') === 'true')
            newFormState.state = 'Done' ;
        setFormState(newFormState);
    }, [location.search]);


    const onInputChange = ({ target }: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = target;
        setFormState({
            ...formState,
            [ name ]: value
        });
    }

    const onResetForm = () => {
        setFormState( {} );
    }


    return (
    <>
        <form className="form-inline p-4" onSubmit={ onSearchSubmit }>

            <div className="form-group row">
                <label className="col-sm-2 col-form-label">Name</label>
                <div className="col-sm-10">
                    <input 
                        type="text" 
                        className="form-control" 
                        id="task" 
                        placeholder="text" 
                        name="taskName"
                        autoComplete="off"
                        value={ taskName }
                        onChange={ onInputChange }
                    />
                </div>
            </div>

            <div className="form-group row">
                <label className="col-sm-2 col-form-label" htmlFor="inlineFormCustomSelectPriority">
                    Priority
                </label>
                <div className="col-sm-10">
                    <select
                        className="form-control"
                        id="inlineFormCustomSelectPriority"
                        name="priority"
                        value={ priority }
                        onChange={ onInputChange }
                    >
                        <option value="All">All</option>
                        <option value="High">High</option>
                        <option value="Medium">Medium</option>
                        <option value="Low">Low</option>
                    </select>
                </div>
            </div>

            <div className="form-group row">
                <label className="col-sm-2 col-form-label" htmlFor="inlineFormCustomSelectState">State</label>
                <div className="col-sm-6">
                    <select 
                        className="form-control" 
                        id="inlineFormCustomSelectState"
                        name="state"
                        value={ state }
                        onChange={ onInputChange }
                    >
                        <option value="All">All</option>
                        <option value="Done">Done</option>
                        <option value="Undone">Undone</option>
                    </select>
                    <div className="col-sm-2 clearfix">
                        <button type="submit" className="btn btn-primary float-right">Search</button>
                    </div>
                </div>
            </div>
        </form>
    </>
    )
}
