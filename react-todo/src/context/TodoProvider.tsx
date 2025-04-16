
import React, { useEffect, useReducer, useState } from "react"
import { TodoContext } from "./TodoContext";
import { parseISO } from "date-fns";
import todoApi from "../api/TodoApi";
import { useLocation } from "react-router";
import { GridSortModel } from "@mui/x-data-grid";
import { MetricsInterface } from "../todo/interfaces/MetricsInterface";
import { ModalInsertFormInput, ModalUpdateFormInput } from "../todo/interfaces/ModalForm";
import { TodoInterface } from "../todo/interfaces/TodoInterface";
import { AxiosResponse } from "axios";
import { TodoResponseInterface } from "../todo/interfaces/TodoResponseInterface";

export const TodoProvider = ({ children }) => {
    const myPageSize: number = 10;


    const [filteredList, setFilteredList] = useState<TodoInterface[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    // const [ todos, dispatch ] = useReducer( todoReducer, [] );
    const [selectedRows, setSelectedRows] = useState([]);

    const location = useLocation();

    const [paginationModel, setPaginationModel] = useState({
        pageSize: myPageSize,
        page: 0,
    });

    const [sortModel, setSortModel] = React.useState<GridSortModel>([]);
    const [rowCount, setRowCount] = useState(0);

    const [metricModel, setMetricModel] = useState<MetricsInterface>({
        averageTimeToFinishByPriority: {
            Low: '',
            Medium: '',
            High: '',
        },
        averageEstimatedTimeToComplete: '', 
    });



    const getAll = async(params?: URLSearchParams ) =>{
        try {
            console.log('params', params);
            if (!params) {
                params = new URLSearchParams();
            }

            if (sortModel.length > 0) {
                params.append('sortBy', sortModel[0].field.toUpperCase());
                params.append('sortDirection', sortModel[0].sort.toUpperCase());
            }
            
            params.append('page', paginationModel.page.toString());
            params.append('size', paginationModel.pageSize.toString());
            
            const response: AxiosResponse<TodoResponseInterface> = await todoApi.get('', {
                params: params,
            });
            
            const { data } = response;

            await getMetrics();
            
            setPaginationModel({
                pageSize: data.fileredTodo.length,
                page: data.currentPage,
            });

            let donesArray:any = [];
            data.fileredTodo.map( (todo:any) =>{
                if (todo.done)
                    donesArray.push(todo.id);
            });

            setSelectedRows(donesArray);
            
            const dataFormatted : TodoInterface[] = data.fileredTodo.map( (todo:any) =>{
                if (todo.dueDate){
                    todo.dueDate = parseISO(todo.dueDate);
                }
                return todo;
            });
            
            setRowCount(data.totalItems);
            setIsLoading(false);
            setFilteredList(dataFormatted);
            
        } catch (error) {
            console.log("Error al cargar");
            console.log(error);
        }
    }

    const getMetrics = async() => {
        const metricsCall = await todoApi.get('/metrics');
        const metrics: MetricsInterface = metricsCall.data;
        setMetricModel(metrics);
    }



    const getById = async(id:String) =>{
        try {
            const {data} = await todoApi.get(`/${id}`);
            await getMetrics();
            return data;
            
        } catch (error) {
            console.log("Error al cargar");
            console.log(error);
        }
    }

    const postTodo = async(formValues:ModalInsertFormInput) =>{
        try {
            console.log('llamando a post back res:', formValues);
            const { data } = await todoApi.post('', formValues,{
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            } );
            await updateRecords();         
        } catch (error) {
            console.log("There was an error");
            console.log(error);
        }
    }

    const updateTodoDone = async(ids:String[]) =>{
        try {
            const promises = ids.map(async (id) => {
                const data:any = await getById(id);
                if (data && !data.done){
                    // POST http://localhost:8080/todos/{id}/done
                    const r= await todoApi.post(`/${data.id}/done`);
                    return r.data;
                }
                else{
                    // PUT http://localhost:8080/todos/{id}/undone
                    const r= await todoApi.put(`/${data.id}/undone`);
                    return r.data;
                }
            });

            const results = await Promise.all(promises);

            setSelectedRows((prevSelectedRows) => {
                let newSelectedRows = [...prevSelectedRows];
                results.forEach((result) => {
                if (result.done) {
                    if (!newSelectedRows.includes(result.id)) {
                        newSelectedRows.push(result.id);
                    }
                } else {
                    newSelectedRows = newSelectedRows.filter((id) => id !== result.id);
                }
                });
                return newSelectedRows;
            });
            await updateRecords();
        } catch (error) {
            console.log("There was an error ");
            console.log(error);
        }
    }
    const updateTodo = async(formValues:ModalUpdateFormInput) =>{
        try {
            const { data } = await todoApi.put(`/${formValues.id}`, formValues );
            await updateRecords();
        } catch (error) {
            console.log("There was an error: ", error);
        }
    } 

    const deleteTodo = async(id:String) =>{
        try {
            const { data } = await todoApi.delete(`/${id}`);
            await updateRecords();
        } catch (error) {
            console.log("Error:", error);
        }
    }


    const updateRecords = async () => {
        try {
            const params = new URLSearchParams(location.search);
            await getAll(params);
            await getMetrics();

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    useEffect(() => {
        
        updateRecords();
    }, [location.search, paginationModel.page,sortModel ]);

    return (
        
        <TodoContext.Provider value={{ filteredList, setFilteredList,
            // todos, dispatch, 
            isLoading, 
            getAll, postTodo, updateTodo, deleteTodo, getById,
            paginationModel, rowCount, setPaginationModel, 
            sortModel, setSortModel, metricModel, updateTodoDone, selectedRows, setSelectedRows
        }}>
            { children }
        </TodoContext.Provider>
    )
}
