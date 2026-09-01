import { useEffect, useState } from 'react'
import api from '../api/client'

const EMPTY = { email: '', password: '', displayName: '', dob: '', gender: 'FEMALE', city: '' }

export default function Landing() {
  const [health, setHealth] = useState(null)
  const [healthError, setHealthError] = useState(null)
  const [healthLoading, setHealthLoading] = useState(true)

  const [mode, setMode] = useState('login')
  const [form, setForm] = useState(EMPTY)
  const [submitting, setSubmitting] = useState(false)
  const [message, setMessage] = useState(null)
  const [user, setUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem('roadnet_user') || 'null')
    } catch {
      return null
    }
  })

  useEffect(() => {
    api
      .get('/health')
      .then((res) => setHealth(res.data))
      .catch((err) => setHealthError(err.response?.data?.message || err.message || 'Backend unreachable'))
      .finally(() => setHealthLoading(false))
  }, [])

  const update = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const submit = async (e) => {
    e.preventDefault()
    setSubmitting(true)
    setMessage(null)
    try {
      const path = mode === 'login' ? '/auth/login' : '/auth/register'
      const payload =
        mode === 'login'
          ? { email: form.email, password: form.password }
          : { email: form.email, password: form.password, displayName: form.displayName, dob: form.dob, gender: form.gender, city: form.city }
      const { data } = await api.post(path, payload)
      localStorage.setItem('roadnet_token', data.token)
      localStorage.setItem('roadnet_user', JSON.stringify(data.user))
      setUser(data.user)
      setMessage({ type: 'success', text: `${mode === 'login' ? 'Logged in' : 'Registered'} as ${data.user.displayName}` })
    } catch (err) {
      setMessage({ type: 'error', text: err.response?.data?.message || 'Request failed' })
    } finally {
      setSubmitting(false)
    }
  }

  const logout = () => {
    localStorage.removeItem('roadnet_token')
    localStorage.removeItem('roadnet_user')
    setUser(null)
    setForm(EMPTY)
  }

  const input = 'w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500'

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 via-white to-brand-50 flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight">
            ROAD<span className="text-brand-600">.NET</span>
          </h1>
          <p className="text-slate-500 mt-2">Human-connection platform · shared foundation</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-6">
          {/* Connection status */}
          <div className="flex items-center justify-between rounded-lg border border-slate-200 px-4 py-3 mb-6">
            <span className="text-sm font-medium text-slate-700">Backend /api/health</span>
            {healthLoading ? (
              <span className="text-xs text-slate-400">checking…</span>
            ) : health ? (
              <span className="inline-flex items-center gap-1.5 text-xs font-semibold text-green-700">
                <span className="h-2 w-2 rounded-full bg-green-500" /> ok
              </span>
            ) : (
              <span className="inline-flex items-center gap-1.5 text-xs font-semibold text-red-600">
                <span className="h-2 w-2 rounded-full bg-red-500" /> offline ({healthError})
              </span>
            )}
          </div>

          {user ? (
            <div className="text-center">
              <div className="h-16 w-16 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center text-xl font-bold mx-auto mb-3">
                {user.displayName?.charAt(0).toUpperCase()}
              </div>
              <h2 className="text-lg font-semibold text-slate-900">{user.displayName}</h2>
              <p className="text-sm text-slate-500">{user.email}</p>
              <p className="text-xs text-slate-400 mt-1">
                {user.city ? `${user.city} · ` : ''}
                {user.verified ? 'Verified' : 'Not verified'} · id {user.id}
              </p>
              <button
                onClick={logout}
                className="mt-5 w-full rounded-lg bg-slate-900 text-white text-sm font-medium py-2 hover:bg-slate-700 transition"
              >
                Log out
              </button>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-2 gap-1 rounded-lg bg-slate-100 p-1 mb-5">
                {['login', 'register'].map((m) => (
                  <button
                    key={m}
                    onClick={() => { setMode(m); setMessage(null) }}
                    className={`rounded-md py-1.5 text-sm font-medium transition ${
                      mode === m ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-500'
                    }`}
                  >
                    {m === 'login' ? 'Log in' : 'Register'}
                  </button>
                ))}
              </div>

              <form onSubmit={submit} className="space-y-3">
                {mode === 'register' && (
                  <>
                    <input name="displayName" value={form.displayName} onChange={update} required placeholder="Display name" className={input} />
                    <input name="dob" type="date" value={form.dob} onChange={update} required className={input} />
                    <select name="gender" value={form.gender} onChange={update} className={input}>
                      <option value="FEMALE">Female</option>
                      <option value="MALE">Male</option>
                      <option value="NON_BINARY">Non-binary</option>
                      <option value="UNDISCLOSED">Undisclosed</option>
                    </select>
                    <input name="city" value={form.city} onChange={update} placeholder="City (optional)" className={input} />
                  </>
                )}
                <input name="email" type="email" value={form.email} onChange={update} required placeholder="Email" className={input} />
                <input name="password" type="password" value={form.password} onChange={update} required placeholder="Password" className={input} />

                {message && (
                  <p className={`text-sm rounded-lg px-3 py-2 ${message.type === 'error' ? 'bg-red-50 text-red-700' : 'bg-green-50 text-green-700'}`}>
                    {message.text}
                  </p>
                )}

                <button
                  type="submit"
                  disabled={submitting}
                  className="w-full rounded-lg bg-brand-600 text-white text-sm font-medium py-2 hover:bg-brand-700 disabled:opacity-60 transition"
                >
                  {submitting ? 'Working…' : mode === 'login' ? 'Log in' : 'Create account'}
                </button>
              </form>

              {mode === 'login' && (
                <p className="mt-4 text-center text-xs text-slate-400">
                  Seed login: <code className="text-slate-500">ravi.sharma@example.com</code> /{' '}
                  <code className="text-slate-500">SeedPass123!</code>
                </p>
              )}
            </>
          )}
        </div>

        <p className="mt-6 text-center text-xs text-slate-400">
          Frontend :5173 · API :8080 · PostgreSQL roadnet · docs/API-CONTRACT.md
        </p>
      </div>
    </div>
  )
}
