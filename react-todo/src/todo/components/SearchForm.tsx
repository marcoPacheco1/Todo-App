import { useContext, useState } from "react";
import { TodoContext } from "../../context/TodoContext";

export const SearchForm = () => {

    const {filteredList, setFilteredList, todos, dispatch, getAll } = useContext( TodoContext );
    
    const [ formState, setFormState ] = useState( {} );

    const { taskName, priority, state } = formState;

    const onResetFilter = () => {
        dispatch({ type: 'Reset Todo', payload: allTodos });
    };

    const onSearchSubmit = async(event) =>{
        // await getAll();
        event.preventDefault();
        // onResetFilter();
        const {target} = event;

        console.log('al presionar filtar:', formState);
        

        // const action = {
        //     type: 'Filter Todo',
        //     payload: formState
        // }
        // dispatch( action );

        let filteredTasks = todos.map(todo => ({ ...todo }));
                          
        if (formState.hasOwnProperty('taskName')){
            filteredTasks= filteredTasks.filter( (todo:TodoFilter) => {
                console.log(todo['taskName'], formState['taskName']);
                console.log(todo['taskName']?.toLocaleLowerCase().includes( formState['taskName'] ));
                
                return todo['taskName']?.toLocaleLowerCase().includes( formState['taskName'] ) 
            });
        }

        if (formState.hasOwnProperty('state')){
            filteredTasks= filteredTasks.filter( todo => {
                if (formState['state'] === 'Undone')
                    return todo['done'] === false 
                else if (formState['state'] === 'Done')
                    return todo['done'] === true 
                return todo    
            });
        }
        
        if (formState.hasOwnProperty('priority')){
            filteredTasks= filteredTasks.filter( todo => {
                if (formState['priority'] !== 'All')
                    return todo['priority'] === formState['priority'] 
                return todo
            });
        }
        setFilteredList(filteredTasks);
        
    }


    const onInputChange = ({ target }) => {
        console.log(target);
        
        const { name, value } = target;
        console.log(name, value);
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
