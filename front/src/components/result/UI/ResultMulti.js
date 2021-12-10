import React from 'react';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import { Grid } from '@mui/material';
import ToggleBtn from '../ToggleBtn';
import { ResultChart, Text } from '../comp/ResultMultiComp';
import MyResponsivePie from '../charts/MyResponsivePie';
import MyResponsiveLine from '../charts/MyResponsiveLine';
import MyResponsiveBar from '../charts/MyResponsiveBar';

const ResultMulti = ({ index, result, chartState, resultKeys, setChartState }) => {
    const newKey = Object.keys(result.resultMap[index]);
    const newValue = Object.values(result.resultMap[index]);
    const newData = []
    for (const key in newValue) {
        newData.push({ "id":newKey[key], "value":newValue[key]});
    }

    const newKey1 = Object.keys(result.resultMap[index]);
    const newValue1 = Object.values(result.resultMap[index]);
    const newData1 = []
    for (const key in newValue1) {
        newData1.push({ "x":newKey1[key], "y":newValue1[key]});
    }
    return (
        <>
            <Paper elevation={3} sx={{ my: { xs: 3, md: 6 }, p: { xs: 2, md: 3 }, backgroundColor:'#E0ECF8' }}>
                <Grid container spacing={2}>
                    <Grid container justifyContent="center">
                        <Grid item xs={11}>
                            <Paper elevation={1} sx={{ my: { xs: 2, md: 2 }, p: { xs: 2, md: 2 } }}>
                                <TextField fullWidth label="질문명" value={result&&result.questionList[index].surQue_Content}  variant="standard"  focused va InputProps={{ readOnly: true}} />
                            </Paper>
                        </Grid>
                    </Grid>
                    <Grid item xs={12} md={6} lg={6}>
                        <Paper elevation={3} sx={{ bgcolor: '#A9D0F5', my: { xs: 1 }, p: { xs: 2 } }}>
                            <Text result={result} index={index} flag={"보기"} resultKeys={resultKeys} />
                            {/* <Text result={result.resultMap} index={index} flag={"보기"} resultKeys={resultKeys} /> */}
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={6} lg={6}>
                        <Paper elevation={3} sx={{ bgcolor: '#81BEF7', my: { xs: 1 }, p: { xs: 2 } }}>
                            <Text result={result} index={index} flag={"결과"} resultKeys={resultKeys} />
                            {/* <Text result={result.resultMap} index={index} flag={"결과"} resultKeys={resultKeys} /> */}
                        </Paper>
                    </Grid>
                    <Grid item xs={12} textAlign="right">
                        <ToggleBtn fullWidth chartState={chartState} setChartState={setChartState} />
                    </Grid>
                    <Grid item xs={12}>
                        <Paper elevation={3} sx={{ bgcolor: '#58ACFA', p: { xs: 2 } }}>
                            <Paper elevation={1}>
                                <div style={{ height: 300 }}>
                                    {
                                        (() => {
                                            switch (chartState) {
                                                case "BarChart":
                                                    return <MyResponsiveBar data={newData}/>;
                                                case "DoughnutChart":
                                                    return <MyResponsivePie data={newData} />;
                                                case "LineChart":
                                                    return <MyResponsiveLine data={[{ "id":'결과', "data":newData1}]} />;
                                                default:
                                                    break;
                                            }
                                        })()
                                    }
                                {/* <BarChart data={result.resultMap[index]}/>
                                <DoughnutChart data={result.resultMap[index]} /> */}
                                </div>
                            </Paper>
                        </Paper>
                    </Grid>
                </Grid>
            </Paper>
        </>
    );
};


export default ResultMulti;