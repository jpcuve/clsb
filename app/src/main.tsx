import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
// import './index.css'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css'
import {StompSessionProvider} from 'react-stomp-hooks'
import {createBrowserRouter, createRoutesFromElements, Route, RouterProvider} from 'react-router-dom'
import Dashboard from './components/Dashboard.tsx'

const router = createBrowserRouter(createRoutesFromElements(
  <Route path="/" element={<App/>}>
    <Route path="dashboard" element={<Dashboard/>}/>
  </Route>
), {
  basename: import.meta.env.VITE_APP_WEB_CONTEXT,
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <MantineProvider>
      <StompSessionProvider url={"http://localhost:8080/clsb/messaging"}>
        <RouterProvider router={router}/>
      </StompSessionProvider>
    </MantineProvider>
  </StrictMode>,
)
