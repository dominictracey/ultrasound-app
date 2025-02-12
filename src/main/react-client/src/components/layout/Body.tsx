/* eslint-disable react/prop-types */
import React, { FC } from 'react'
import Content from '../content/Content'
import Footer from './Footer'
import Header from './Header'
// import { useAppSelector, useAppDispatch } from '../../redux/hooks'

const Body: FC = () => {
    return (
        <div className="body">
            <Header />
            <Content />
            <Footer />
        </div>
    )
}

export default Body
