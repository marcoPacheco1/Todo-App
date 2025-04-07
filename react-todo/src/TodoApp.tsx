import { BrowserRouter } from "react-router"
import { AppRouter } from "./router/AppRouter"

export const TodoApp = () => {
  return (
    <BrowserRouter>
        <AppRouter />
    </BrowserRouter>
  )
}
