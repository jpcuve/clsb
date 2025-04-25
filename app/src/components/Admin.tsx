import {Stack} from '@mantine/core'
import {Outlet} from 'react-router-dom'

const Admin = () => {
  return (
    <Stack>
      <Outlet/>
    </Stack>
  )
}

export default Admin
