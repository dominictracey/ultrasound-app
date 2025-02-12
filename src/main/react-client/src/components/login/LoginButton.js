import React from 'react'
import { Link } from 'react-router-dom'

const LoginButton = () => (
    <Link to="/login">
        <div className="form-group" style={{ marginLeft: '1rem' }}>
            <button type="button" className="btn btn-outline-primary">
                Log in
            </button>
        </div>
    </Link>
)

export default LoginButton
