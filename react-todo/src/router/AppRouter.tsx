// npm i react-router
// https://reactrouter.com/start/declarative/installation

import {  Route, Routes} from "react-router"
import { TodoPage } from "../todo/components/TodoPage"
import { TodoProvider } from "../context/TodoProvider"

export const AppRouter = () => {

    return (
        <TodoProvider>

            <Routes>
                <Route path='/*' element={<TodoPage />}> </Route>
            </Routes>
        </TodoProvider>
    )
}
