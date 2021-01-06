#include <chrono>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <streambuf>
#include <string>

std::string read(const std::string &filepath)
{
  std::ifstream ifs(filepath, std::ios::ate);

  auto end = ifs.tellg();
  ifs.seekg(0, std::ios::beg);

  auto size = std::size_t(end - ifs.tellg());

  if (size == 0) // avoid undefined behavior
    return {};

  std::string buffer(size, 0);

  if (!ifs.read((char *)buffer.data(), size))
    throw std::runtime_error("Could not read file: " + filepath);

  return buffer;
}

int main(int argc, char *argv[])
{
  auto start = std::chrono::high_resolution_clock::now();
  // std::ifstream input("input.txt");

  // std::string str((std::istreambuf_iterator<char>(input)),
  //                 std::istreambuf_iterator<char>());
  std::string str = read("input.txt");

  auto stop = std::chrono::high_resolution_clock::now();
  auto nanos = stop - start;
  std::cout << "Done in " << (nanos.count() / (1000)) << " us (" << str.size() << " characters)" << std::endl;

  return 0;
}