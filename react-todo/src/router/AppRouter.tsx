// npm i react-router
// https://reactrouter.com/start/declarative/installation

import { Navigate, Route, Routes } from "react-router"
import { TodoPage } from "../todo/components/TodoPage"

export const AppRouter = () => {
    return (

        <Routes>
            <Route path='/*' element={<TodoPage />}> </Route>

            {/* {
                (authStatus === 'non-authenticated')
                ? <Route path='/auth/*' element={}> </Route>
                // : <Route path='/*' element={<CalendarPage/>}> </Route>
                
                // <Route path='/*' element={ <Navigate to="/auth/login" /> }> </Route>
                
            } */}

        </Routes>
    )
}
