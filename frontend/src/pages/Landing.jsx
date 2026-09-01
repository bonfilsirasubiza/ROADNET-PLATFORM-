import { useEffect, useState } from 'react'
import api from '../api/client'

export default function Landing() {
  const [health, setHealth] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api
      .get('/health')
      .then((res) => {
        setHealth(res.data)
        setError(null)
      })
      .catch((err) => {
        setError(err.response?.data?.message || err.message || 'Backend unreachable')
        setHealth(null)
      })
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-6">
      <div className="max-w-lg w-full bg-white rounded-2xl shadow-lg p-8 text-center">
        <h1 className="text-3xl font-bold text-slate-900 mb-2">ROAD.NET</h1>
        <p className="text-slate-500 mb-8">
          The human-connection platform — shared foundation
        </p>

        <div className="border-t border-slate-200 pt-6">
          <h2 className="text-sm uppercase tracking-wide text-slate-400 mb-4">
            Frontend ↔ Backend connectivity
          </h2>

          {loading && <p className="text-slate-500">Checking /api/health…</p>}

          {!loading && error && (
            <div className="bg-red-50 text-red-700 rounded-lg px-4 py-3 text-sm">
              Failed to reach backend: {error}
            </div>
          )}

          {!loading && !error && health && (
            <div className="bg-green-50 text-green-700 rounded-lg px-4 py-3 text-sm">
              <span className="font-semibold">Backend healthy.</span>{' '}
              GET /api/health →{' '}
              <code className="bg-green-100 rounded px-1.5 py-0.5">
                {JSON.stringify(health)}
              </code>
            </div>
          )}
        </div>

        <p className="mt-8 text-xs text-slate-400">
          Seed users: see docs/API-CONTRACT.md · password{' '}
          <code className="text-slate-500">SeedPass123!</code> · Postgres{' '}
          <code className="text-slate-500">localhost:5432/roadnet</code>
        </p>
      </div>
    </div>
  )
}
