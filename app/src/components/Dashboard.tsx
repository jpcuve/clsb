import {Text, Stack} from '@mantine/core'
import {FC, useContext} from 'react'
import {PerpetualContext} from './Admin.tsx'

const Dashboard: FC = () => {
  const perpetual = useContext(PerpetualContext);
  return (
    <Stack>
      <Text>Admin dashboard</Text>
      <Text>{JSON.stringify(perpetual)}</Text>
    </Stack>
  )
}

export default Dashboard
