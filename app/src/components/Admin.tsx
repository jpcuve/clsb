import {useContext} from 'react'
import {Perpetual} from '../entities.ts'
import {Group, Stack, Text} from '@mantine/core'
import {Outlet} from 'react-router-dom'
import PerpetualContext from "../contexts/PerpetualContext.ts";

const Admin = () => {
  const perpetual = useContext<Perpetual>(PerpetualContext)
  return (
    <Stack>
      <Group>
        <Text>{perpetual.bank.baseIso}</Text>
      </Group>
      <Outlet/>
    </Stack>
  )
}

export default Admin
