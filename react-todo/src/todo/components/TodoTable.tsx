import React, { useContext, useEffect, useReducer, useState } from "react";
import { TodoContext } from "../../context/TodoContext";

import { DataGrid, GridColDef, GridRowParams, GridRowsProp } from '@mui/x-data-grid';
import { EditTodoButton } from "./EditTodoButton";
import { format } from "date-fns";
import { TodoInterface } from "../interfaces/TodoInterface";



export const TodoTable = () => {

    const { dispatch, setFilteredList, filteredList, deleteTodo, getById, updateTodo,
        paginationModel, setPaginationModel, rowCount, sortModel, setSortModel, updateTodoDone,
        selectedRows, setSelectedRows
     } = useContext( TodoContext );


    const columnKeys = () =>{
        if (filteredList && filteredList.length > 0) {
            return [ "taskName", "priority", "dueDate"];
        }
        return [];
    };

    const handleDelete = (id: number) => {
        console.log('Borrar ID:', id);
        deleteTodo(id);
        
        setFilteredList(filteredList.filter( (todo:TodoInterface) => todo.id !== id));
    };


    // useEffect(() => {
    //     setFilteredList((filteredList: TodoInterface[]) =>
    //         filteredList.map((filteredTodo: TodoInterface) => {
    //         const updatedTodo = todos.find((todo: TodoInterface) => todo.id === filteredTodo.id);
    //         return updatedTodo ? updatedTodo : filteredTodo;
    //       })
    //     );
    // }, [todos]);

    const handleToggleDone = async(newRowsSelected: String[]) => {
        let difference: String[] | null = [];

        if (newRowsSelected.length > selectedRows.length) {
            newRowsSelected.forEach((id:String) => {
                console.log(id);
                
                if (!selectedRows.includes(id)) {
                    difference?.push(id);                    
                }
            });
        } else if (newRowsSelected.length < selectedRows.length) {
            selectedRows.forEach((id:String) => {
                if (!newRowsSelected.includes(id)) {
                    difference?.push(id);                    
                }
            });
        }
        await updateTodoDone(difference);

    };

    const getColumns = () => {
        const arr: GridColDef[] = [];

        columnKeys().map(key => {
            let headerName = key;
            if (key === "taskName") {
                headerName = "Name";
            } else if (key === "priority") {
                headerName = "Priority";
            } else if (key === "dueDate") {
                headerName = "Due Date";
            }
            
            if (key !== 'done')
                arr.push({
                    field: key, headerName: headerName, flex: 1
                });
        })
        arr.push({field: 'actions', headerName: 'Actions', flex: 1, renderCell: (params) => (
            <div>
              <EditTodoButton id = {params.row.id}/>
              <button className="m-2 btn btn-outline-dark" onClick={() => handleDelete(params.row.id)}>Delete</button>
            </div>)
            });
        
        return arr;
    };

    const getRows = () => {

        const arr: GridRowsProp[] = [];
        filteredList.map((todo:TodoInterface) => {
            const rowData: { id: string; [key: string]: any } = { id: todo.id };

            columnKeys().forEach((key) => {
                if (key === 'dueDate'){
                    const dueDate = todo[key];
                    if (dueDate !== null)
                    {
                        rowData[key] = format(todo[key], 'yyyy-MM-dd');
                    }
                }
                else
                    rowData[key] = todo[key];
            });
            arr.push(rowData);
        });
        return arr;
    };  
    
    const columns: GridColDef[] = getColumns();
    const rows: GridRowsProp[] = getRows();

    const getRowStyle = (params: GridRowParams) => {
        if ( params.row.dueDate == null) return;
        const dateFormatted = new Date(params.row.dueDate);

        const now = new Date();
        const timeDifferenceMs = Math.abs(dateFormatted.getTime() - now.getTime());
        // Calculate the number of milliseconds in a week
        const millisecondsInWeek = 1000 * 60 * 60 * 24 * 7;
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
        return {};
    };

    const handlePaginationModelChange = (newPaginationModel) => {
        // newPaginationModel : {page: 1, pageSize: 2}
        setPaginationModel(newPaginationModel);
    };

    const handleSortModelChange = (newSortModel) => {
        // sortModel : {field: 'id', sort: 'asc'}
        setSortModel(newSortModel);
    };
    return (
        <div className="m-3">
            <div style={{ height: '100%', width: '100%' }} >
                <DataGrid
                    checkboxSelection
                    rows={rows}
                    columns={columns}
                    getRowClassName={getRowStyle}
                    rowCount={rowCount}
                    paginationModel={paginationModel}
                    onPaginationModelChange={handlePaginationModelChange}
                    paginationMode="server"
                    sortingMode="server"
                    sortModel={sortModel}
                    onSortModelChange={handleSortModelChange}
                    rowSelectionModel={selectedRows}
                    onRowSelectionModelChange={handleToggleDone}    
                    disableRowSelectionOnClick                
                />
            </div>
        </div>
    )
}
