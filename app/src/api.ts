const api: any = {
  token: async (code: string, scope: string) => {
    const search = new URLSearchParams()
    search.append('grant_type', 'authorization_code')
    search.append('code', code)
    search.append('redirect_uri', `${window.location.protocol}//${window.location.host}${window.location.pathname}`)
    search.append('scope', scope)
    const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/token`,  {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: search,
    })
    return res.json()
  },

  userInfo: async (token: string) => {
    const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/userinfo`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return res.json()
  },

  banks: async (token: string) => {
    const res = await fetch(`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/api/banks`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return res.json()
  }

}

export default api
