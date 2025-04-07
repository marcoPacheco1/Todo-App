import { useContext, useReducer } from "react";
import { TodoContext } from "../../context/TodoContext";

import { DataGrid, GridColDef, GridRenderCellParams, GridRowParams, GridRowsProp } from '@mui/x-data-grid';
import { TodoInterface } from "../interfaces/TodoInterface";
import { todoReducer } from "../../context/TodoReducer";
import { Checkbox } from "@mui/material";



export const TodoTable = () => {


    // const rows: GridRowsProp = [
    //     { id: 1, col1: 'Hello', col2: 'World' },
    //     { id: 2, col1: 'DataGridPro', col2: 'is Awesome' },
    //     { id: 3, col1: 'MUI', col2: 'is Amazing' },
    // ];

    // const columns: GridColDef[] = [
    //     { field: 'col1', headerName: 'Column 1', width: 150 },
    //     { field: 'col2', headerName: 'Column 2', width: 150 },
    // ];

    // const columnKeys = useMemo(() => {
    //     if (todos && todos.length > 0) {
    //       return Object.keys(todos[0]) as (keyof Todo)[];
    //     }
    //     return [];
    //   }, [todos]);

    // const rows1: GridRowsProp = [
    //     { id: 1, col1: 'Hello', col2: 'World' },
    //     { id: 2, col1: 'DataGridPro', col2: 'is Awesome' },
    //     { id: 3, col1: 'MUI', col2: 'is Amazing' },
    // ];


    const { todos, dispatch } = useContext( TodoContext );

    const columnKeys = () =>{
        if (todos && todos.length > 0) {
            return Object.keys(todos[0]) as (keyof TodoInterface)[];
        }
        return [];
    };


    const handleEdit = (row: TodoInterface) => {
        console.log('Editar:', row);
        
        const action = {
            type: '[TODO] Add Todo',
            payload: newTodo
        }
        dispatch( action );
        // Implementa tu lógica de edición aquí
    };

    const handleDelete = (id: number) => {
        console.log('Borrar ID:', id);
        // Implementa tu lógica de borrado aquí
        const action = {
            type: 'Delete Todo',
            payload: id
        }

        dispatch( action );
    };

    const handleToggleDone = (id: number) => {
        console.log('done:', id);
        // Implementa tu lógica de borrado aquí
        const action = {
            type: 'Toogle Done',
            payload: id
        }

        dispatch( action );
    };

    const getColumns = () => {
        const arr: GridColDef[] = [{
            field: 'done',
            headerName: 'Done',
            width: 80,
            renderCell: (params: GridRenderCellParams<TodoInterface, boolean>) => (
              <Checkbox
                checked={params.value}
                onChange={(event) => handleToggleDone(params.row.id)}
              />
            ),
          }];
        columnKeys().map(key => {
            if (key !== 'done')
                arr.push({
                    field: key, headerName: key, width: 150
                });
        })
        arr.push({field: 'actions', headerName: 'actions', width: 150, renderCell: (params) => (
            <div>
              <button className="btn" onClick={() => handleEdit(params.row)}>Edit</button>
              <button className="btn" onClick={() => handleDelete(params.row.id)}>Delete</button>
            </div>)
            });
        
        return arr;
    };


    const getRows = () => {
        const arr: GridRowsProp[] = [];
        todos.map((todo) => {
            const rowData: { id: number; [key: string]: any } = { id: todo.id };

            columnKeys().forEach((key) => {
              rowData[key] = todo[key];
            });
            console.log(rowData);
            // rowData['action'] = 
            arr.push(rowData);
        });
        return arr;
    };

    const columns: GridColDef[] = getColumns();
    const rows: GridRowsProp[] = getRows();

    const getRowStyle = (params: GridRowParams) => {

        if ( params.row.dueDate == null) return;

        const now = new Date();
        const timeDifferenceMs = Math.abs(params.row.dueDate.getTime() - now.getTime());
        // Calculate the number of milliseconds in a week
        const millisecondsInWeek = 1000 * 60 * 60 * 24 * 7;

        // Convert the time difference to weeks
        const weeks = timeDifferenceMs / millisecondsInWeek;
        
        
        if (weeks <= 1) {
            // One week between due date and today
            return 'bg-danger'
        }
        else if (weeks <=2) {
            // 2 weeks between due date and today – Yellow background color
            return 'bg-warning'
        }
        else if (weeks >2) {
            // More that 2 weeks between due date and today
            return 'bg-success'
        }
        return {}; // Color por defecto
    };

    return (
        <>
            <div>TodoTable</div>
            {/* pagination is enabled by default and cannot be disabled. */}
            <div style={{ height: 300, width: '100%' }}>
                <DataGrid
                    initialState={{
                        pagination: {
                          paginationModel: { pageSize: 3, page: 0 },
                        },
                    }}
                    rows={rows}
                    columns={columns}
                    getRowClassName={getRowStyle}
                />
            </div>
        </>
    )
}
