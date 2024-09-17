import {Bank} from '../entities.ts'
import {FC} from 'react'
import {Box} from '@mantine/core'

const AccountListView: FC<{bank: Bank}> = props => {
  return (
    <Box>{props.bank.denomination}: All accounts</Box>
  )

}

export default AccountListView
