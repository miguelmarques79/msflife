import React, { useState, useEffect } from 'react';
import { Container, FormControl, InputLabel, Select, MenuItem, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField } from '@mui/material';
import './App.css';

function App() {
    const [years, setYears] = useState([]);
    const [selectedYear, setSelectedYear] = useState('');
    const [data, setData] = useState([]);
    const [editIndex, setEditIndex] = useState(null);
    const [editData, setEditData] = useState({});

    useEffect(() => {
        fetchYears();
    }, []);

    useEffect(() => {
        if (selectedYear) {
            fetchDataByYear(selectedYear);
        }
    }, [selectedYear]);

    const fetchYears = async () => {
        try {
            const response = await fetch('http://localhost:8080/mortality/');
            const result = await response.json();
            setYears(result);
        } catch (error) {
            console.error('Erro ao buscar anos:', error);
        }
    };

    const fetchDataByYear = async (year) => {
        try {
            const response = await fetch(`http://localhost:8080/mortality/${year}`);
            const result = await response.json();
            setData(result);
        } catch (error) {
            console.error('Erro ao buscar dados:', error);
        }
    };

    const handleEdit = (index) => {
        setEditIndex(index);
        setEditData(data[index]);
    };

    const handleCancel = () => {
        setEditIndex(null);
        setEditData({});
    };

    const handleConfirm = () => {
        const updatedData = [...data];
        updatedData[editIndex] = editData;
        setData(updatedData);
        setEditIndex(null);
        setEditData({});
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEditData({ ...editData, [name]: value });
    };

    return (
        <Container>
            <h1>Selecione o Ano</h1>
            <FormControl fullWidth>
                <InputLabel id="year-select-label">Ano</InputLabel>
                <Select
                    labelId="year-select-label"
                    value={selectedYear}
                    onChange={(e) => setSelectedYear(e.target.value)}
                >
                    <MenuItem value="">
                        <em>Selecione um ano</em>
                    </MenuItem>
                    {years.map(year => (
                        <MenuItem key={year} value={year}>{year}</MenuItem>
                    ))}
                </Select>
            </FormControl>

            <h2>Dados do Ano {selectedYear}</h2>
            <Card>
                <CardContent>
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>ID</TableCell>
                                    <TableCell>Tx Feminina</TableCell>
                                    <TableCell>Tx Masculina</TableCell>
                                    <TableCell>Ações</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data.map((item, index) => (
                                    <TableRow key={item.id}>
                                        <TableCell>{item.id}</TableCell>
                                        <TableCell>
                                            {editIndex === index ? (
                                                <TextField
                                                    name="txMale"
                                                    value={editData.txMale}
                                                    onChange={handleChange}
                                                />
                                            ) : (
                                                item.txMale
                                            )}
                                        </TableCell>
                                        <TableCell>
                                            {editIndex === index ? (
                                                <TextField
                                                    name="txFemale"
                                                    value={editData.txFemale}
                                                    onChange={handleChange}
                                                />
                                            ) : (
                                                item.txFemale
                                            )}
                                        </TableCell>
                                        <TableCell>
                                            {editIndex === index ? (
                                                <>
                                                    <Button onClick={handleConfirm}>Confirmar</Button>
                                                    <Button onClick={handleCancel}>Cancelar</Button>
                                                </>
                                            ) : (
                                                <Button onClick={() => handleEdit(index)}>Editar</Button>
                                            )}
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </CardContent>
            </Card>
        </Container>
    );
}

export default App;