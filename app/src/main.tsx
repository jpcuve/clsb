import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
// import './index.css'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css'
import {StompSessionProvider} from 'react-stomp-hooks'
import {createBrowserRouter, Navigate, RouterProvider} from 'react-router-dom'
import Dashboard from './components/Dashboard.tsx'
import Test from './components/Test.tsx'
import {Notifications} from "@mantine/notifications";

const router = createBrowserRouter([
  {path: '/', element: <App/>, children: [
      {path: 'test', element: <Test/>},
      {path: 'dashboard', element: <Dashboard/>},
    ]},
  {path: '*', element: <Navigate to={"/"} replace/>}
], {
  basename: import.meta.env.VITE_APP_WEB_CONTEXT,
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
  <MantineProvider theme={{
    primaryColor: 'PB',
    colors: {
      'R' : ['#ffd9e0', '#ffb1c0', '#ff86a1', '#ff4d81', '#ea0064', '#bc004f', '#90003b', '#660028', '#3f0016', '#000000'],
      'YR' : ['#ffdbcb', '#ffb68c', '#fd8f00', '#d77900', '#b26300', '#8e4e00', '#6c3a00', '#4c2700', '#2e1500', '#000000'],
      'Y' : ['#ffe25f', '#e4c600', '#c5aa00', '#a69000', '#897600', '#6d5e00', '#524600', '#393000', '#211b00', '#000000'],
      'GY' : ['#b4f700', '#9dd900', '#87bb00', '#729e00', '#5d8200', '#496800', '#364e00', '#243600', '#131f00', '#000000'],
      'G' : ['#53ffab', '#00e38e', '#00c47a', '#00a666', '#008953', '#006d41', '#005230', '#003920', '#002110', '#000000'],
      'BG' : ['#00fdeb', '#00dece', '#00c0b2', '#00a296', '#00867c', '#006a62', '#00504a', '#003732', '#00201d', '#000000'],
      'B' : ['#afecff', '#00d9fb', '#00bbd9', '#009fb8', '#008398', '#006879', '#004e5c', '#003640', '#001f26', '#000000'],
      'PB' : ['#d7e3ff', '#abc7ff', '#79acff', '#2692ff', '#0078d8', '#005fae', '#004785', '#00315e', '#001c3a', '#000000'],
      'P' : ['#eedbff', '#e0b7ff', '#d28fff', '#c762ff', '#be0cff', '#9900cf', '#75009f', '#520071', '#320047', '#000000'],
      'RP' : ['#ffd7f2', '#fface6', '#ff7cdc', '#ff30d4', '#db00b5', '#b00091', '#86006e', '#5f004d', '#3b002f', '#000000'],
    },
  }}>
    <StompSessionProvider url={`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/messaging`}>
      <RouterProvider router={router}/>
    </StompSessionProvider>
    <Notifications/>
  </MantineProvider>
  </StrictMode>,
)
