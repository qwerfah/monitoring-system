#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:5.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src
COPY ["GeneratorService/GeneratorService.csproj", "GeneratorService/"]
RUN dotnet restore "GeneratorService/GeneratorService.csproj"
COPY . .
WORKDIR "/src/GeneratorService"
RUN dotnet build "GeneratorService.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "GeneratorService.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "GeneratorService.dll"]